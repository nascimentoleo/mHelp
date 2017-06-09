package com.ifma.appmhelp.models;

/**
 * Created by leo on 11/12/16.
 */
public class Host {
    private String endereco = "192.168.1.24";
    //private String endereco = "10.14.0.40";
    //private String endereco = "192.168.0.8";
    //private String endereco = "10.0.2.2"; //For avd
    private int porta = 5222;

    public Host(String endereco, int porta) {
        this.porta = porta;
        this.endereco = endereco;
    }

    public Host(){

    }

    public String getEndereco() {
        return endereco;
    }

    public int getPorta() {
        return porta;
    }
}
