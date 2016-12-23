package com.ifma.appmhelp.daos;

import com.ifma.appmhelp.models.IModel;

import java.sql.SQLException;

/**
 * Created by leo on 11/30/16.
 */
public interface IDao {

    boolean persistir(IModel objeto, boolean updateChild) throws SQLException;

    void carregaId(IModel objeto) throws SQLException;

    String getMsgErro();
}
