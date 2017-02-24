package com.ifma.appmhelp.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by leo on 2/24/17.
 */

@DatabaseTable(tableName = "ocorrencias")
public class Ocorrencia implements IModel {

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField(canBeNull = false)
    private String titulo;
    @DatabaseField
    private Date data;
    @ForeignCollectionField
    private ForeignCollection<Mensagem> mensagens;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Paciente paciente;

    Ocorrencia(){

    }

    public Ocorrencia(String titulo, Date data, Paciente paciente) {
        this.titulo = titulo;
        this.data = data;
        this.paciente = paciente;
    }

    public ForeignCollection<Mensagem> getMensagens() {
        return mensagens;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
