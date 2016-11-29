package com.ifma.appmhelp.db;

/**
 * Created by leo on 11/28/16.
 */
public class DbInfo {
    private static final String NOME_BANCO = "mhelpdb";
    private static final int VERSAO_BANCO = 4;

    public static int getVersaoBanco() {
        return VERSAO_BANCO;
    }

    public static String getNomeBanco() {
        return NOME_BANCO;
    }
}
