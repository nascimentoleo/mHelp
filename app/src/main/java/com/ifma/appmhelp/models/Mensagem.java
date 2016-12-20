package com.ifma.appmhelp.models;

import com.google.gson.Gson;
import com.ifma.appmhelp.enums.TipoDeMensagem;

/**
 * Created by leo on 11/3/16.
 */
public class Mensagem implements IModel{

    private String msg;
    private TipoDeMensagem tipo;

    public Mensagem(String msg, TipoDeMensagem tipo) {
        this.msg  = msg;
        this.tipo = tipo;
    }

    public String getMsg() {
        return msg;
    }

    public TipoDeMensagem getTipo() {
        return tipo;
    }

    public String toJson(){
        return new Gson().toJson(this);
    }
    public static Mensagem fromJson(String jsonObject){
        return new Gson().fromJson(jsonObject, Mensagem.class);
    }

    //Ainda não implementado
    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long id) {

    }
}
