package com.ifma.appmhelp.daos;

import java.sql.SQLException;

/**
 * Created by leo on 11/30/16.
 */
public interface IDao<T> {

    boolean persistir(T objeto, boolean updateChild) throws SQLException;

    public void remover(T objeto, boolean updateChild) throws SQLException;

    void carregaId(T objeto) throws SQLException;

    String getMsgErro();
}
