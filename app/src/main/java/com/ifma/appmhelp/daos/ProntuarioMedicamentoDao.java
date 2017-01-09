package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Prontuario;
import com.ifma.appmhelp.models.ProntuarioMedicamento;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 12/23/16.
 */

public class ProntuarioMedicamentoDao extends BaseController implements IDao {


    public ProntuarioMedicamentoDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(IModel objeto, boolean updateChild) throws SQLException {
        Dao<ProntuarioMedicamento,Long> dao = DbSqlHelper.getHelper(ctx).getDao(ProntuarioMedicamento.class);
        dao.createOrUpdate((ProntuarioMedicamento) objeto);
        return true;
    }

    @Override
    public void remover(IModel objeto, boolean updateChild) throws SQLException {
        Dao<ProntuarioMedicamento, Long> dao = DbSqlHelper.getHelper(ctx).getDao(ProntuarioMedicamento.class);
        dao.delete((ProntuarioMedicamento) objeto);
    }

    public List<ProntuarioMedicamento> getProntuariosMedicamentos(Prontuario prontuario) throws SQLException {
        Dao<ProntuarioMedicamento,Long> dao = DbSqlHelper.getHelper(ctx).getDao(ProntuarioMedicamento.class);
        ProntuarioMedicamento prontuarioMedicamento = new ProntuarioMedicamento(prontuario, null, null);
        return dao.queryForMatching(prontuarioMedicamento);
    }

    @Override
    public void carregaId(IModel objeto) throws SQLException {
        Dao<ProntuarioMedicamento, Long> dao = DbSqlHelper.getHelper(ctx).getDao(ProntuarioMedicamento.class);
        List<ProntuarioMedicamento> prontuarioMedicamentos = dao.queryForMatching((ProntuarioMedicamento) objeto);
        if(!prontuarioMedicamentos.isEmpty())
            objeto.setId(prontuarioMedicamentos.get(0).getId());

    }



}
