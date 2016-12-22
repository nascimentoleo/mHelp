package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.MedicoPaciente;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by leo on 12/20/16.
 */

public class MedicoPacienteController extends BaseController implements IController {

    public MedicoPacienteController(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(IModel objeto, boolean updateChild) throws SQLException {
        MedicoPaciente medicoPaciente = (MedicoPaciente) objeto;
        if (updateChild){
           new MedicosController(ctx).persistir(medicoPaciente.getMedico(),updateChild);
           new PacientesController(ctx).persistir(medicoPaciente.getPaciente(),updateChild);
        }

        Dao<MedicoPaciente, Long> dao = DbSqlHelper.getHelper(ctx).getDao(MedicoPaciente.class);
        dao.createOrUpdate(medicoPaciente);
        return true;
    }

    @Override
    public void carregaId(IModel objeto) throws SQLException {

    }

}
