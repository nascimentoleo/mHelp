package com.ifma.appmhelp.models;

import com.google.gson.Gson;
import com.ifma.appmhelp.enums.StatusSolicitacaoRoster;

/**
 * Created by leo on 12/20/16.
 */

public class SolicitacaoRoster {

    private Usuario usuario;
    private StatusSolicitacaoRoster status;

    public SolicitacaoRoster(Usuario usuario, StatusSolicitacaoRoster status) {
        this.setUsuario(usuario);
        this.status = status;
    }

    public StatusSolicitacaoRoster getStatus() {
        return status;
    }

    public IModel getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.usuario.setSenha(null);
    }

    public String toJson(){
        return new Gson().toJson(this);
    }

    public static SolicitacaoRoster fromJson(String jsonObject){
        return new Gson().fromJson(jsonObject, SolicitacaoRoster.class);
    }
}

