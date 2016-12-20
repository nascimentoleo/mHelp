package com.ifma.appmhelp.models;

/**
 * Created by leo on 12/20/16.
 */

public class UsuarioLogado {
    private IModel modelo;
    private static UsuarioLogado instance = null;

    public synchronized static UsuarioLogado getInstance() {
        if(instance==null)
            instance = new UsuarioLogado();
        return instance;
    }

    public void setModelo(IModel modelo) {
        this.modelo = modelo;
    }

    public IModel getModelo() {
        return modelo;
    }
}
