package com.ifma.appmhelp.models;

/**
 * Created by leo on 11/28/16.
 */
public class Medico {
    private String nome;
    private String crm;

    public Medico(String nome, String crm) {
        this.nome = nome;
        this.crm = crm;
    }

    public String getCrm() {
        return crm;
    }

    public String getNome() {
        return nome;
    }
}
