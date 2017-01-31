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

import com.ifma.appmhelp.controls.ClientXMPPController;
import com.ifma.appmhelp.enums.ConexaoXMPPKeys;
import com.ifma.appmhelp.models.ConexaoXMPP;

import org.jivesoftware.smack.AbstractXMPPConnection;


public class ConexaoXMPPService extends Service {

    private ConectarXMPPTask conectarTask;
    private AbstractXMPPConnection conexao;
    private final IBinder mBinder = new LocalBinder();

    private BroadcastReceiver mReceiverConectar = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra(ConexaoXMPPKeys.CONECTOU.toString(), false)) {
                conexao = conectarTask.getConexao();
                ConexaoXMPP.getInstance().setConexao(conexao);
            }
        }
    };

    private BroadcastReceiver mReceiverAutenticou = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initNotification();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverConectar, new IntentFilter(ConexaoXMPPKeys.CONECTAR.toString()));
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverAutenticou, new IntentFilter(ConexaoXMPPKeys.AUTENTICOU.toString()));

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

        try {
            conectarTask = ClientXMPPController.conectar(this, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    //Necessário para que o serviço rode em foreground, mesmo com a janela fechada
    private void initNotification(){
        Notification notification = ServiceNotification.createNotification(this);
        startForeground(ServiceNotification.ID_NOTIFICATION,notification);

    }


    public class LocalBinder extends Binder{

        public ConexaoXMPPService getService() {
            return ConexaoXMPPService.this;
        }
    }

}

