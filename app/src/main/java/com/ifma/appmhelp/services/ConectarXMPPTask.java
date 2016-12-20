package com.ifma.appmhelp.services;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.ifma.appmhelp.factories.FactoryConexaoXMPP;
import com.ifma.appmhelp.models.Host;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * Created by leo on 11/12/16.
 */
public class ConectarXMPPTask extends AsyncTask<Host, Integer, Boolean> {

    private String msgErro;
    private AbstractXMPPConnection conexao;
    private Context ctx;

    public AbstractXMPPConnection getConexao() {
        return conexao;
    }

    public ConectarXMPPTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public Boolean doInBackground(Host... params){
        try {
            this.conexao = FactoryConexaoXMPP.getConexao(this.ctx, params[0].getEndereco(), params[0].getPorta());
            return true;
        } catch (IOException | SmackException | XMPPException e) {
            e.printStackTrace();
            msgErro = e.getMessage();
            return false;
        }

    }

    @Override
    protected void onPostExecute(Boolean response) {
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this.ctx);
        Intent it = new Intent("conectar");

        if(this.conexao != null) {
            it.putExtra("finalizou_conexao", true);
        }else{
            it.putExtra("finalizou_conexao", false);
            it.putExtra("msg_erro", this.msgErro);
        }

        lbm.sendBroadcast(it);

    }

}
