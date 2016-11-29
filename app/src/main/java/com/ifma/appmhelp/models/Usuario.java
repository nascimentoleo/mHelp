package com.ifma.appmhelp.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by leo on 11/12/16.
 */

@DatabaseTable(tableName = "usuarios")
public class Usuario {

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField(uniqueIndex = true, canBeNull = false)
    private String login;
    @DatabaseField (canBeNull = false)
    private String senha;
    @DatabaseField
    private String cpf;
    @DatabaseField
    private String nome;
    @DatabaseField
    private String email;

    public Usuario(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    public Long getId() {
        return id;
    }

    //Construtor sem argumentos necessário para o funcionamento do orm
    public Usuario() {

    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
