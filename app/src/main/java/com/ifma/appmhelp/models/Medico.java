package com.ifma.appmhelp.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by leo on 11/28/16.
 */

@DatabaseTable(tableName = "medicos")
public class Medico implements IModel,Cloneable{
    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField
    private String crm;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Usuario usuario;

    public Medico() {
    }

    public Medico(Usuario usuario) {
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

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void preparaParaEnvio(){
        this.id = null;
        this.usuario.preparaParaEnvio();
    }

    @Override
    public Medico clone() throws CloneNotSupportedException {
        return (Medico) super.clone();
    }

}
