package com.ifma.appmhelp.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by leo on 12/23/16.
 */

@DatabaseTable(tableName = "cids")
public class Cid implements IModel {

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField(canBeNull = false)
    private String codigo;
    @DatabaseField
    private String descricao;

    public Cid(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    Cid(){

    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public boolean equals(Object objeto) {
        if (objeto == null)
            return false;
        if (objeto == this)
            return true;
        if (!(objeto instanceof Cid))
            return false;
        return (this.getId() == ((Cid)objeto).getId());
    }
}
