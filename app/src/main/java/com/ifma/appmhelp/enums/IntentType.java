package com.ifma.appmhelp.enums;

/**
 * Created by leo on 12/29/16.
 */

public enum IntentType {
    SOLICITACAO_ROSTER("SOLICITACAO_ROSTER"),
    FINALIZAR_SERVICO("FINALIZAR_SERVICO"),
    ATUALIZAR_OCORRENCIAS("ATUALIZAR_OCORRENCIAS");

    private String value;

    private IntentType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
