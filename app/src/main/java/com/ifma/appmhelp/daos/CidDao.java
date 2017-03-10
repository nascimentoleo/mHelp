package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.Cid;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 12/23/16.
 */

public class CidDao extends BaseController implements IDao<Cid> {


    public CidDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(Cid objeto, boolean updateChild) throws SQLException {
        return false;
    }

    @Override
    public void remover(Cid objeto, boolean updateChild) throws SQLException {

    }

    @Override
    public void carregaId(Cid objeto) throws SQLException {

    }

    public List<Cid> getCids(Long inicio, Long fim) throws SQLException {
        Dao<Cid, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Cid.class);
        return dao.queryBuilder().offset(inicio).limit(fim).query();
    }

    public List<Cid> getTodos(int qtdRegistros) throws SQLException {
        Dao<Cid, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Cid.class);
        return dao.queryForAll();
    }

    public List<Cid> getCidsByField(Long inicio, Long fim, String fieldName, String fieldValue) throws SQLException {
        Dao<Cid, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Cid.class);
        QueryBuilder<Cid, Long> query = dao.queryBuilder().offset(inicio).limit(fim);
        query.where().like(fieldName, "%" + fieldValue + "%");
        PreparedQuery<Cid> prepare = query.prepare();
        return dao.query(prepare);
    }


}
