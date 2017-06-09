package com.ifma.appmhelp.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ifma.appmhelp.controls.ClientXMPPController;
import com.ifma.appmhelp.enums.ConexaoXMPPKeys;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by leo on 1/31/17.
 */
public class ConexaoXMPPListener implements ConnectionListener {

    private Context context;

    public ConexaoXMPPListener(Context context) {
        this.context = context;
    }

    @Override
    public void connected(XMPPConnection connection) {
        Log.d("SMACK", "Conectado");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        Log.d("SMACK", "Autenticado");
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this.context);
        Intent it = new Intent(ConexaoXMPPKeys.AUTENTICOU.toString());
        lbm.sendBroadcast(it);
    }

    @Override
    public void connectionClosed() {
        Log.d("SMACK", "Conexão fechada");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.d("SMACK", "Conexão fechada por erro: " + e.getMessage());
        //Reconectar
        ClientXMPPController.conectar(context);
    }

    @Override
    public void reconnectionSuccessful() {
        Log.d("SMACK", "Reconectado");
    }

    @Override
    public void reconnectingIn(int seconds) {
        Log.d("SMACK", "Reconectando em: " + Integer.toString(seconds) + " s");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.d("SMACK", "Reconexão falhou: " + e.getMessage());
    }
}
