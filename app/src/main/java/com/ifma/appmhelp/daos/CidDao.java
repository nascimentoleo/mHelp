package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.Cid;
import com.ifma.appmhelp.models.IModel;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

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
    public void remover(IModel objeto, boolean updateChild) throws SQLException {

    }

    @Override
    public void carregaId(IModel objeto) throws SQLException {

    }

    public List<Cid> getTodos() throws SQLException {
        Dao<Cid, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Cid.class);
        return dao.queryForAll();
    }


}
