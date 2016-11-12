package com.ifma.appmhelp.models;

/**
 * Created by leo on 11/12/16.
 */
public class Host {
    private String endereco;
    private int porta;

    public Host(String endereco, int porta) {
        this.porta = porta;
        this.endereco = endereco;
    }

    public String getEndereco() {
        return endereco;
    }

    public int getPorta() {
        return porta;
    }
}
