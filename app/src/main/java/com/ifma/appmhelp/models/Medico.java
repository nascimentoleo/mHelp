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

    public Medico(String crm) {
        this.crm = crm;
    }

    public Medico() {
    }

    public Long getId() {
        return id;
    }

    public String getCrm() {
        return crm;
    }
}
