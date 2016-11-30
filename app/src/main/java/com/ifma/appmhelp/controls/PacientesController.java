package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Usuario;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 11/29/16.
 */
public class PacientesController extends BaseController implements IController {

    public PacientesController(Context ctx) {
        super(ctx);
    }

    public Paciente getMedicoByUsuario(Usuario usuario) throws SQLException {
        Dao<Paciente, Long> medicoDao = DbSqlHelper.getHelper(ctx).getDao(Paciente.class);
        List<Paciente> pacientes = medicoDao.queryForMatching(new Paciente(usuario));
        if (!pacientes.isEmpty())
            return pacientes.get(0);
        return null;
    }

    @Override
    public boolean persistir(IModel objeto) throws SQLException {
        Dao<Paciente,Long> pacienteDao = DbSqlHelper.getHelper(ctx).getDao(Paciente.class);
        pacienteDao.createOrUpdate((Paciente) objeto);
        return true;
    }
}
