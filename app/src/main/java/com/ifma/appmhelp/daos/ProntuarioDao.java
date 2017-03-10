package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.Prontuario;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by leo on 12/23/16.
 */

public class ProntuarioDao extends BaseController implements IDao<Prontuario> {


    public ProntuarioDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(Prontuario objeto, boolean updateChild) throws SQLException {
        Dao<Prontuario,Long> dao = DbSqlHelper.getHelper(ctx).getDao(Prontuario.class);
        dao.createOrUpdate(objeto);
        return true;
    }

    @Override
    public void remover(Prontuario objeto, boolean updateChild) throws SQLException {
        Dao<Prontuario,Long> dao = DbSqlHelper.getHelper(ctx).getDao(Prontuario.class);
        dao.delete(objeto);
    }

    @Override
    public void carregaId(Prontuario objeto) throws SQLException {

    }
}
