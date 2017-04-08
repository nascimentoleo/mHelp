package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.Anexo;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by leo on 4/8/17.
 */
public class AnexoDao extends BaseController implements IDao<Anexo>{


    public AnexoDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(Anexo objeto, boolean updateChild) throws SQLException {
        Dao<Anexo, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Anexo.class);
        dao.createOrUpdate(objeto);
        return true;
    }

    @Override
    public void remover(Anexo objeto, boolean updateChild) throws SQLException {
        Dao<Anexo,Long> dao = DbSqlHelper.getHelper(ctx).getDao(Anexo.class);
        dao.delete(objeto);
    }

    @Override
    public void carregaId(Anexo objeto) throws SQLException {

    }
}
