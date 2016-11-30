package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Usuario;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 11/29/16.
 */
public class PacientesController extends BaseController {

    public PacientesController(Context ctx) {
        super(ctx);
    }

    public Paciente getMedicoByUsuario(Usuario usuario) throws SQLException {
        DbSqlHelper databaseHelper = OpenHelperManager.getHelper(this.ctx, DbSqlHelper.class);
        Dao<Paciente, Long> medicoDao = databaseHelper.getDao(Paciente.class);
        List<Paciente> pacientes = medicoDao.queryForMatching(new Paciente(usuario));
        if (!pacientes.isEmpty())
            return pacientes.get(0);
        return null;
    }

    @Override
    public boolean persistir(IModel objeto) throws SQLException {
        DbSqlHelper databaseHelper = OpenHelperManager.getHelper(ctx, DbSqlHelper.class);
        Dao<Paciente,Long> pacienteDao = databaseHelper.getDao(Paciente.class);
        pacienteDao.createOrUpdate((Paciente) objeto);
        return true;
    }
}
