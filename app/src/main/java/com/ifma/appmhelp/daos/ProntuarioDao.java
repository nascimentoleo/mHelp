package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Prontuario;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by leo on 12/23/16.
 */

public class ProntuarioDao extends BaseController implements IDao {


    public ProntuarioDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(IModel objeto, boolean updateChild) throws SQLException {
        Prontuario prontuario = (Prontuario) objeto;
        Dao<Prontuario,Long> dao = DbSqlHelper.getHelper(ctx).getDao(Prontuario.class);
        dao.createOrUpdate(prontuario);
        return true;
    }

    @Override
    public void remover(IModel objeto, boolean updateChild) throws SQLException {
        Prontuario prontuario = (Prontuario) objeto;
        Dao<Prontuario,Long> dao = DbSqlHelper.getHelper(ctx).getDao(Prontuario.class);
        dao.delete(prontuario);
    }

    @Override
    public void carregaId(IModel objeto) throws SQLException {

    }
}
