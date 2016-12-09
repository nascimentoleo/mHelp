package com.ifma.appmhelp.enums;

/**
 * Created by leo on 12/2/16.
 */
public enum BundleKeys {
    USUARIO_LOGADO("USUARIO_LOGADO");

    private final String value;

    BundleKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
