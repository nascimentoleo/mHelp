package com.ifma.appmhelp.services;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ifma.appmhelp.controls.ClientXMPPController;
import com.ifma.appmhelp.enums.ConexaoXMPPKeys;
import com.ifma.appmhelp.models.ConexaoXMPP;

import org.jivesoftware.smack.AbstractXMPPConnection;


public class ConexaoXMPPService extends Service {

    private ConectarXMPPTask conectarTask;
    private NotificationListener notificationListener;
    private AbstractXMPPConnection conexao;
    private final IBinder mBinder = new LocalBinder();
    private boolean reconectando;

    private BroadcastReceiver mReceiverConectar = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            verificaConexao(intent.getBooleanExtra(ConexaoXMPPKeys.CONECTOU.toString(), false));
        }
    };

    private BroadcastReceiver mReceiverAutenticou = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initNotification("Conectado", "Você está conectado");
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverConectar, new IntentFilter(ConexaoXMPPKeys.CONECTAR.toString()));
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverAutenticou, new IntentFilter(ConexaoXMPPKeys.AUTENTICOU.toString()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
         return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            //if(intent.getAction().equals(ConexaoXMPPKeys.CONECTAR.getValue())){
               this.conectar();
            //}
        }
        return super.onStartCommand(intent, flags, startId);

    }

    private boolean conectar(){
        if(this.conexao != null){
            if(this.conexao.isConnected()){
               ConexaoXMPP.getInstance().setConexao(this.conexao);
               return true;
            }
        }
        conectarTask = ClientXMPPController.conectar(this);

        return true;
    }

    private void reconectar(){
        new Thread(){
            @Override
            public void run() {
               try {
                    Thread.currentThread().sleep(3000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }finally {
                   Log.d("Smack","Tentando reconectar...");
                   conectar();
               }
            }
        }.start();
    }

    private void verificaConexao(boolean conectou){
        if(conectou) {
            this.conexao = this.conectarTask.getConexao();
            ConexaoXMPP.getInstance().setConexao(this.conexao);
            if (reconectando)
                initNotification("Conectado", "Você está conectado");

        }else {
            if (this.conexao != null){
                this.conexao = null;
                this.reconectando = true;
                initNotification("Reconectando", "Conexão perdida, Reconectando");
            }
            this.reconectar();

        }
    }

    //Necessário para que o serviço rode em foreground, mesmo com a janela fechada
    private void initNotification(String msg, String ticker){
        Notification notification = ServiceNotification.createNotification(this, msg, ticker);
        startForeground(ServiceNotification.ID_NOTIFICATION,notification);

        this.notificationListener = new NotificationListener(this);

    }

    public class LocalBinder extends Binder{

        public ConexaoXMPPService getService() {
            return ConexaoXMPPService.this;
        }
    }

}

