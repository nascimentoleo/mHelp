package com.ifma.appmhelp.models;

import com.ifma.appmhelp.lib.BlowfishCript;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by leo on 11/12/16.
 */

@DatabaseTable(tableName = "usuarios")
public class Usuario implements IModel{

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

    public Usuario(String login, String senha) throws Exception {
        this.login = login;
        //this.senha = senha;
        this.setSenha(senha);
    }

    public Usuario(String login) {
        this.login = login;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getSenhaPlain() throws Exception {
        return BlowfishCript.decrypt(senha);
    }

    public String getSenhaCript() throws Exception {
        return this.senha;
    }

    private void setSenha(String senha) throws Exception {
        this.senha = BlowfishCript.encrypt(senha);
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
