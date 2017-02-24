package com.ifma.appmhelp.models;

import com.google.gson.Gson;
import com.ifma.appmhelp.enums.TipoDeMensagem;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by leo on 11/3/16.
 */

@DatabaseTable(tableName = "mensagens")
public class Mensagem implements IModel{

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField(canBeNull = false)
    private String msg;
    @DatabaseField(canBeNull = false)
    private TipoDeMensagem tipo;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Ocorrencia ocorrencia;

    Mensagem(){

    }

    public Mensagem(String msg, TipoDeMensagem tipo) {
        this.msg  = msg;
        this.tipo = tipo;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Ocorrencia getOcorrencia() {
        return ocorrencia;
    }

    public void setOcorrencia(Ocorrencia ocorrencia) {
        this.ocorrencia = ocorrencia;
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


}
