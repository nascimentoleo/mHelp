package com.ifma.appmhelp.models;

import com.ifma.appmhelp.enums.Sexo;
import com.ifma.appmhelp.enums.TipoSanguineo;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by leo on 12/23/16.
 */

@DatabaseTable(tableName = "prontuarios")
public class Prontuario implements IModel {

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField
    private Sexo sexo;
    @DatabaseField
    private TipoSanguineo tipoSanguineo;
    @DatabaseField
    private String nomeDaMae;
    @DatabaseField
    private String nomeDoPai;
    @DatabaseField
    private  String telefoneDoResponsavel;
    @DatabaseField
    private  String observacoes;
    @ForeignCollectionField
    private ForeignCollection<Medicamento> medicamentos;
    @ForeignCollectionField
    private ForeignCollection<ProntuarioCid> cids;

    public Prontuario() {

    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public TipoSanguineo getTipoSanguineo() {
        return tipoSanguineo;
    }

    public void setTipoSanguineo(TipoSanguineo tipoSanguineo) {
        this.tipoSanguineo = tipoSanguineo;
    }

    public String getNomeDaMae() {
        return nomeDaMae;
    }

    public void setNomeDaMae(String nomeDaMae) {
        this.nomeDaMae = nomeDaMae;
    }

    public String getNomeDoPai() {
        return nomeDoPai;
    }

    public void setNomeDoPai(String nomeDoPai) {
        this.nomeDoPai = nomeDoPai;
    }

    public String getTelefoneDoResponsavel() {
        return telefoneDoResponsavel;
    }

    public void setTelefoneDoResponsavel(String telefoneDoResponsavel) {
        this.telefoneDoResponsavel = telefoneDoResponsavel;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public ForeignCollection<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public ForeignCollection<ProntuarioCid> getCids() {
        return cids;
    }
}