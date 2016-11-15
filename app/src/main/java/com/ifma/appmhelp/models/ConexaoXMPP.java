package com.ifma.appmhelp.models;

import org.jivesoftware.smack.AbstractXMPPConnection;

import java.io.Serializable;

/**
 * Created by leo on 11/14/16.
 */
public class ConexaoXMPP implements Serializable{

    private AbstractXMPPConnection conexao;

    private static ConexaoXMPP instance = null;

    public synchronized static ConexaoXMPP getInstance() {
        if(instance==null){
            instance = new ConexaoXMPP();
        }
        return instance;
    }

    public void setConexao(AbstractXMPPConnection conexao) {
        this.conexao = conexao;
    }

    public AbstractXMPPConnection getConexao() {
        return conexao;
    }

    public boolean conexaoEstaAtiva(){
        if(this.conexao != null)
            if(this.conexao.isConnected())
                return true;
        return false;
    }
}
