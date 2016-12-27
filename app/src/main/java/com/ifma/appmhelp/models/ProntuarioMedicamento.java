package com.ifma.appmhelp.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by leo on 12/26/16.
 */

@DatabaseTable(tableName = "prontuarios_medicamentos")
public class ProntuarioMedicamento implements IModel {

    private Long id;
    @DatabaseField
    private String doses;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Prontuario prontuario;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Medicamento medicamento;

    ProntuarioMedicamento(){

    }

    public ProntuarioMedicamento(String doses, Prontuario prontuario, Medicamento medicamento) {
        this.doses = doses;
        this.prontuario = prontuario;
        this.medicamento = medicamento;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getDoses() {
        return doses;
    }

    public void setDoses(String doses) {
        this.doses = doses;
    }

    public Prontuario getProntuario() {
        return prontuario;
    }

    public void setProntuario(Prontuario prontuario) {
        this.prontuario = prontuario;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }
}
