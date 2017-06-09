package com.ifma.appmhelp.enums;

/**
 * Created by leo on 4/3/17.
 */

public enum RequestType {
    ABRIR_CAMERA(0),
    ABRIR_GALERIA(1);

    private int value;

    RequestType(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }

}
