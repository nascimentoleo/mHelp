package com.ifma.appmhelp.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by leo on 12/23/16.
 */

@DatabaseTable(tableName = "prontuarios_cids")
public class ProntuarioCid implements IModel {

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField(foreign = true,  foreignAutoRefresh = true)
    private Prontuario prontuario;
    @DatabaseField(foreign = true,  foreignAutoRefresh = true)
    private Cid cid;

    public ProntuarioCid(Prontuario prontuario, Cid cid) {
        this.prontuario = prontuario;
        this.cid = cid;
    }

    public ProntuarioCid(){

    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Prontuario getProntuario() {
        return prontuario;
    }

    public Cid getCid() {
        return cid;
    }
}
