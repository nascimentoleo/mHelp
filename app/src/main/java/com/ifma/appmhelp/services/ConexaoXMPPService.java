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

import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.Host;

import org.jivesoftware.smack.AbstractXMPPConnection;


public class ConexaoXMPPService extends Service {

    private ConectarXMPPTask conectarTask;
    private AbstractXMPPConnection conexao;

    private BroadcastReceiver mReceiverConectar = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra("finalizou_conexao", false)) {
                conexao = conectarTask.getConexao();
                ConexaoXMPP.getInstance().setConexao(conexao);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverConectar, new IntentFilter("conectar"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
         return new LocalBinder();
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
        conectarTask = new ConectarXMPPTask(getApplicationContext());
        conectarTask.execute(new Host("192.168.1.24", 5222));
        //conectarTask.execute(new Host("192.168.0.8", 5222));
        //conectarTask.execute(new Host("10.0.2.2",5222)); //For avd
        return true;
    }

    //Necessário para que o serviço rode em foreground, mesmo com a janela fechada
    public void initNotification(){
        Notification notification = ServiceNotification.createNotification(this);
        startForeground(ServiceNotification.ID_NOTIFICATION,notification);

    }

    public class LocalBinder extends Binder {

        ConexaoXMPPService getService() {
            return ConexaoXMPPService.this;
        }
    }


}

