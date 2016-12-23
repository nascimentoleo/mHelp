package com.ifma.appmhelp.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by leo on 12/16/16.
 */
@DatabaseTable(tableName = "medicos_pacientes")
public class MedicoPaciente implements IModel {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Medico medico;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Paciente paciente;

    public MedicoPaciente(Medico medico, Paciente paciente) {
        this.medico =  medico;
        this.paciente = paciente;
    }

    public MedicoPaciente() {
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long id) {

    }

    public Medico getMedico() {
        return medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }
}
