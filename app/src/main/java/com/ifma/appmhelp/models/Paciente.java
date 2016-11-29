package com.ifma.appmhelp.models;

/**
 * Created by leo on 11/28/16.
 */
public class Paciente {
    private String endereco;
    private String telefone;

    public Paciente() {
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }
}
