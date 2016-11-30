package com.ifma.appmhelp.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by leo on 11/28/16.
 */

@DatabaseTable(tableName = "medicos")
public class Medico {
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

    public Long getId() {
        return id;
    }

    public String getCrm() {
        return crm;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
