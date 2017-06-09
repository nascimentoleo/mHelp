package com.ifma.appmhelp.controls;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 3/9/17.
 */

public abstract class Pagination<T> implements Serializable {

    protected int qtdDeRegistros;
    public static int FIRST = 0;

    public Pagination(int qtdDeRegistros) {
        this.qtdDeRegistros = qtdDeRegistros;
    }

    //Default
    public Pagination(){
        this.qtdDeRegistros = 20;
    }

    public abstract List<T> getRegistros(int offset) throws SQLException;

    public void setQtdDeRegistros(int qtdDeRegistros) {
        this.qtdDeRegistros = qtdDeRegistros;
    }
}
