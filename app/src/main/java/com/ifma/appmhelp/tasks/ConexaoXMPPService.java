package com.ifma.appmhelp.tasks;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.Host;

import org.jivesoftware.smack.AbstractXMPPConnection;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class ConexaoXMPPService extends Service {

    private ConectarXMPPTask conectarTask;
    private AbstractXMPPConnection conexao;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra("finalizou_conexao", false)) {
                conexao = conectarTask.getConexao();
                ConexaoXMPP.getInstance().setConexao(conexao);
            }else
                Toast.makeText(getApplicationContext(), intent.getStringExtra("msg_erro"), Toast.LENGTH_SHORT).show();
        }
    };

    private void initNotification(){
        Intent notificationIntent = new Intent(this, ConexaoXMPP.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("mHelp App").build();
                //.setContentIntent(pendingIntent).build();

        startForeground(1337, notification);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //initNotification();
        conectarTask = new ConectarXMPPTask(getApplicationContext());
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("conectar"));

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
         return new LocalBinder(this);
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

    private void conectar(){
        if(this.conexao == null){
            //conectarTask.execute(new Host("192.168.1.24", 5222));
            conectarTask.execute(new Host("192.168.0.7", 5222));
        }else
            ConexaoXMPP.getInstance().setConexao(this.conexao);
    }

    public class LocalBinder extends Binder {
        ConexaoXMPPService service;

        public LocalBinder(ConexaoXMPPService service) {
            this.service = service;
        }

        public ConexaoXMPPService getService() {
            return service;
        }
    }
}
