package com.ifma.appmhelp.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by leo on 11/12/16.
 */

@DatabaseTable(tableName = "usuarios")
public class Usuario implements IModel, Serializable{

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField(uniqueIndex = true, canBeNull = false)
    private String login;
    @DatabaseField (canBeNull = false)
    private String senha;
    @DatabaseField
    private String nome;
    @DatabaseField
    private String email;

    //Construtor sem argumentos necessário para o funcionamento do orm
    public Usuario() {

    }

    public Usuario(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    public Usuario(String login) {
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
