package com.ifma.appmhelp.enums;

/**
 * Created by leo on 4/8/17.
 */

public enum TipoAnexo {
    IMAGEM("Imagem"), VIDEO("Vídeo");

    private String value;

    TipoAnexo(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
