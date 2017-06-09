package com.ifma.appmhelp.models;

import com.ifma.appmhelp.enums.TipoAnexo;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by leo on 4/8/17.
 */

@DatabaseTable(tableName = "anexos")
public class Anexo implements IModel {

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField(canBeNull = false)
    private String path;
    @DatabaseField
    private TipoAnexo tipoAnexo;


    public Anexo(String path, TipoAnexo tipoAnexo) {
        this.path = path;
        this.tipoAnexo = tipoAnexo;
    }

    Anexo(){

    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public TipoAnexo getTipoAnexo() {
        return tipoAnexo;
    }
}
