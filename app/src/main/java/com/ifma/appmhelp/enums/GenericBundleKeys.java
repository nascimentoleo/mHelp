package com.ifma.appmhelp.enums;

/**
 * Created by leo on 12/2/16.
 */
public enum GenericBundleKeys {
    USUARIO("USUARIO"),
    PACIENTE("PACIENTE"),
    OCORRENCIA("OCORRENCIA"),
    MODIFICOU_PRONTUARIO("MODIFICOU_PRONTUARIO"),
    PRONTUARIO("PRONTUARIO"),
    EDITAR_PRONTUARIO("EDITAR_PRONTUARIO");

    private final String value;

    private GenericBundleKeys(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
