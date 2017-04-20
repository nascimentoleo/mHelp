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
    EDITAR_PRONTUARIO("EDITAR_PRONTUARIO"),
    MENSAGEM("MENSAGEM"),
    ANEXO("ANEXO"),
    TIPO_ANEXO("TIPO_ANEXO");

    private final String value;

    GenericBundleKeys(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
