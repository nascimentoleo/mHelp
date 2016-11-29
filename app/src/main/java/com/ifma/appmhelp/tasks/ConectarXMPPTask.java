package com.ifma.appmhelp.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.Host;
import com.ifma.appmhelp.factories.FactoryConexaoXMPP;

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

    public ConectarXMPPTask(ProgressDialog progressLoad) {
        this.progressLoad = progressLoad;
    }

    @Override
    protected void onPreExecute() {
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
        this.progressLoad.dismiss();
        ConexaoXMPP.getInstance().setConexao(this.conexao);
    }

    public String getMsgErro() {
        return this.msgErro;
    }

}
