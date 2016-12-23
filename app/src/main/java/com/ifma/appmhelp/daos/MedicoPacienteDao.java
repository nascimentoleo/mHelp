package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.MedicoPaciente;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by leo on 12/20/16.
 */

public class MedicoPacienteDao extends BaseController implements IDao {

    public MedicoPacienteDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(IModel objeto, boolean updateChild) throws SQLException {
        MedicoPaciente medicoPaciente = (MedicoPaciente) objeto;
        if (updateChild){
           new MedicosDao(ctx).persistir(medicoPaciente.getMedico(),updateChild);
           new PacientesDao(ctx).persistir(medicoPaciente.getPaciente(),updateChild);
        }

        Dao<MedicoPaciente, Long> dao = DbSqlHelper.getHelper(ctx).getDao(MedicoPaciente.class);
        dao.createOrUpdate(medicoPaciente);
        return true;
    }

    @Override
    public void carregaId(IModel objeto) throws SQLException {

    }

}
