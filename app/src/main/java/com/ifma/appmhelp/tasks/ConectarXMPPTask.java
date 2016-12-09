package com.ifma.appmhelp.tasks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.ifma.appmhelp.factories.FactoryConexaoXMPP;
import com.ifma.appmhelp.models.ConexaoXMPP;
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
    private ProgressDialog progressLoad;



    public AbstractXMPPConnection getConexao() {
        return conexao;
    }

    public ConectarXMPPTask(ProgressDialog progressLoad) {
        this.progressLoad = progressLoad;
    }

    @Override
    protected void onPreExecute() {
        if (progressLoad != null)
             progressLoad = ProgressDialog.show(progressLoad.getContext(), "Por favor aguarde",
                "Iniciando conexão ...");
    }

    @Override
    public Boolean doInBackground(Host... params){
        try {
            this.conexao = FactoryConexaoXMPP.getConexao(params[0].getEndereco(), params[0].getPorta());
            return true;
        } catch (IOException | SmackException | XMPPException e) {
            e.printStackTrace();
            msgErro = e.getMessage();
            return false;
        }

    }

    @Override
    protected void onPostExecute(Boolean response) {
        if(this.progressLoad != null)
            this.progressLoad.dismiss();
        ConexaoXMPP.getInstance().setConexao(this.conexao);
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this.progressLoad.getContext());
        Intent it = new Intent("conectar");
        it.putExtra("finalizou_conexao", true);
        lbm.sendBroadcast(it);
    }

    public String getMsgErro() {
        return this.msgErro;
    }

}
