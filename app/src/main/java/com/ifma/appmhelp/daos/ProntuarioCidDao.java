package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.ProntuarioCid;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by leo on 12/23/16.
 */

public class ProntuarioCidDao extends BaseController implements IDao {


    public ProntuarioCidDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(IModel objeto, boolean updateChild) throws SQLException {
        Dao<ProntuarioCid,Long> dao = DbSqlHelper.getHelper(ctx).getDao(ProntuarioCid.class);
        dao.createOrUpdate((ProntuarioCid) objeto);
        return true;
    }

    @Override
    public void remover(IModel objeto, boolean updateChild) throws SQLException {

    }

    @Override
    public void carregaId(IModel objeto) throws SQLException {

    }



}
