package com.ifma.appmhelp.tasks;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ifma.appmhelp.models.Host;

import org.jivesoftware.smack.AbstractXMPPConnection;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class ConexaoXMPPService extends Service {

    private AbstractXMPPConnection conexao;
    private boolean terminou;
    private ConectarXMPPTask conectar;

    @Override
    public void onCreate() {
        super.onCreate();
        conectar = new ConectarXMPPTask(null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
         return new ConexaoXMPPBinder(this.conexao);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            final String action = intent.getAction();
            //LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
            if(this.conexao == null){
                conectar.execute(new Host("192.168.1.24", 5222));
                this.conexao = conectar.getConexao();
                //Intent it = new Intent(ConexaoXMPPKeys.CONECTAR.getValue());
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    public class ConexaoXMPPBinder extends Binder{

        private AbstractXMPPConnection conexao;

        public ConexaoXMPPBinder(AbstractXMPPConnection conexao) {
            this.conexao = conexao;
        }

        public AbstractXMPPConnection getConexao() {
            return conexao;
        }

    }
}
