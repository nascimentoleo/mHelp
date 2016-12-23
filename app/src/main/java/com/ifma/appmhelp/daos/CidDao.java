package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.models.IModel;

import java.sql.SQLException;

/**
 * Created by leo on 12/23/16.
 */

public class CidDao extends BaseController implements IDao {


    public CidDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(IModel objeto, boolean updateChild) throws SQLException {
        return false;
    }

    @Override
    public void carregaId(IModel objeto) throws SQLException {

    }
}
