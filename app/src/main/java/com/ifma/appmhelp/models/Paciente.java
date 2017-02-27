package com.ifma.appmhelp.models;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by leo on 11/28/16.
 */

@DatabaseTable(tableName = "pacientes")
public class Paciente implements IModel, Cloneable{
    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField
    private String endereco;
    @DatabaseField
    private String telefone;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Usuario usuario;
    @DatabaseField (foreign = true, foreignAutoRefresh = true)
    private Prontuario prontuario;

    public Paciente() {
    }

    public Paciente(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public Prontuario getProntuario() {
        return prontuario;
    }

    public void setProntuario(Prontuario prontuario) {
        this.prontuario = prontuario;
    }

    public String toJson(){
        return new Gson().toJson(this);
    }

    public static Paciente fromJson(String jsonObject){
        return new Gson().fromJson(jsonObject, Paciente.class);
    }
    @Override
    public Paciente clone() throws CloneNotSupportedException {
        return (Paciente) super.clone();
    }

    public void preparaParaEnvio(){
        this.id = null;
        this.usuario.preparaParaEnvio();
    }
}
