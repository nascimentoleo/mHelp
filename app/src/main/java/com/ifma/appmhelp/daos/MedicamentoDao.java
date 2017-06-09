package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.Medicamento;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 12/23/16.
 */

public class MedicamentoDao extends BaseController implements IDao<Medicamento> {


    public MedicamentoDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(Medicamento objeto, boolean updateChild) throws SQLException {
        return false;
    }

    @Override
    public void remover(Medicamento objeto, boolean updateChild) throws SQLException {

    }

    @Override
    public void carregaId(Medicamento objeto) throws SQLException {

    }

    public List<Medicamento> getMedicamentos(Long offset, Long limit) throws SQLException {
        Dao<Medicamento, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Medicamento.class);
        return dao.queryBuilder().offset(offset).limit(limit).query();
    }

    public List<Medicamento> getMedicamentosByField(Long offset, Long limit, String fieldName, String fieldValue) throws SQLException {
        Dao<Medicamento, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Medicamento.class);
        QueryBuilder<Medicamento, Long> query = dao.queryBuilder().offset(offset).limit(limit);
        query.where().like(fieldName, "%" + fieldValue + "%");
        PreparedQuery<Medicamento> prepare = query.prepare();
        return dao.query(prepare);
    }
}
