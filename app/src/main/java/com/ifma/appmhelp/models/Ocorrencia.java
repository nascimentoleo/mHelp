package com.ifma.appmhelp.models;

import com.j256.ormlite.field.DatabaseField;
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
    private Date dataUltimaMensagem;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Paciente paciente;
    private Mensagem ultimaMensagem;

    Ocorrencia(){

    }

    public Ocorrencia(String titulo, Paciente paciente) {
        this.titulo = titulo;
        this.paciente = paciente;
    }

    public Ocorrencia(Paciente paciente) {
        this.paciente = paciente;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public String getTitulo() {
        return titulo;
    }

    public Date getDataUltimaMensagem() {
        return dataUltimaMensagem;
    }

    public void setDataUltimaMensagem(Date dataUltimaMensagem) {
        this.dataUltimaMensagem = dataUltimaMensagem;
    }

    public Mensagem getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(Mensagem ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }
}
