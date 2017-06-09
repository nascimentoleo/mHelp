package com.ifma.appmhelp.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by leo on 12/23/16.
 */

@DatabaseTable(tableName = "medicamentos")
public class Medicamento implements IModel {

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField
    private String nome;

    public Medicamento(String nome) {
        this.nome = nome;
    }

    public Medicamento(){

    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public boolean equals(Object objeto) {
        if (objeto == null)
            return false;
        if (objeto == this)
            return true;
        if (!(objeto instanceof Medicamento))
            return false;
        return (this.getId() == ((Medicamento)objeto).getId());
    }

}
