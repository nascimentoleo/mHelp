package com.ifma.appmhelp.enums;

/**
 * Created by leo on 12/29/16.
 */

public enum IntentType {
    SOLICITACAO_ROSTER("SOLICITACAO_ROSTER");

    private String value;

    private IntentType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
