package com.ifma.appmhelp.enums;

/**
 * Created by leo on 12/23/16.
 */

public enum TipoSanguineo {
    A_POSTIVO("A+"), A_NEGATIVO("A-"), B_POSITIVO("B+"), B_NEGATIVO("B-"),
    AB_POSITIVO("AB+"), AB_NEGATIVO("AB-"), O_POSITIVO("O+"), O_NEGATIVO("O-");

    private String value;

    TipoSanguineo(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
