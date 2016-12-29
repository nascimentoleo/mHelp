package com.ifma.appmhelp.enums;

/**
 * Created by leo on 12/9/16.
 */
public enum ConexaoXMPPKeys {
    CONECTAR("CONECTAR"),
    CONECTOU("CONECTOU"),
    MSG_ERRO("MSG_ERRO");

    private final String value;

    ConexaoXMPPKeys(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
