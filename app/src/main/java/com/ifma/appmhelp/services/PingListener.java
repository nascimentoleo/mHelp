package com.ifma.appmhelp.services;


import android.content.Context;
import android.util.Log;

import com.ifma.appmhelp.controls.ClientXMPPController;

import org.jivesoftware.smackx.ping.PingFailedListener;

/**
 * Created by leo on 1/31/17.
 */

public class PingListener implements PingFailedListener {

    private Context context;

    public PingListener(Context context) {
        this.context = context;
    }

    @Override
    public void pingFailed() {
        Log.d("SMACK", "Conexão Perdida, Reconectando");
        //Reconectar
        ClientXMPPController.conectar(context);
    }
}
