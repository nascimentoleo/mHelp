package com.ifma.appmhelp.enums;

/**
 * Created by leo on 4/3/17.
 */

public enum CameraIntent {

    CAMERA("Abrir Câmera"),
    GALERIA("Abrir Galeria"),
    CANCELAR("Cancelar");
    private final String value;

    CameraIntent(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
