package com.ifma.appmhelp.enums;

/**
 * Created by leo on 12/29/16.
 */

public enum SolicitacaoBundleKeys {
    USUARIO_MEDICO("USUARIO_MEDICO"), FINALIZOU("FINALIZOU"), ACEITOU_SOLICITACAO("ACEITOU_SOLICITACAO");

    private String value;

    SolicitacaoBundleKeys(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
