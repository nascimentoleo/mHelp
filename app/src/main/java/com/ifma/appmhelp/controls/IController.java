package com.ifma.appmhelp.controls;

import com.ifma.appmhelp.models.IModel;

import java.sql.SQLException;

/**
 * Created by leo on 11/30/16.
 */
public interface IController <T>{

    boolean persistir(IModel objeto) throws SQLException;

    void carregaId(IModel objeto) throws SQLException;

    String getMsgErro();
}