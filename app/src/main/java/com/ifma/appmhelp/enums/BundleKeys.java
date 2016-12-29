package com.ifma.appmhelp.enums;

/**
 * Created by leo on 12/2/16.
 */
public enum BundleKeys {
    USUARIO_LOGADO("USUARIO_LOGADO"),
    PACIENTE("PACIENTE");

    private final String value;

    private BundleKeys(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
