package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Usuario;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by leo on 11/29/16.
 */
public class MedicosController extends BaseController{

    public MedicosController(Context ctx) {
        super(ctx);
    }

    public Medico getMedicoByUsuario(Usuario usuario) throws SQLException {
        DbSqlHelper databaseHelper = OpenHelperManager.getHelper(this.ctx, DbSqlHelper.class);
        Dao<Medico, Long> medicoDao = databaseHelper.getDao(Medico.class);
        List<Medico> medicos = medicoDao.queryForMatching(new Medico(usuario));
        if (!medicos.isEmpty())
            return medicos.get(0);
        return null;
    }

    @Override
    public boolean persistir(IModel objeto) throws SQLException {
        DbSqlHelper databaseHelper = OpenHelperManager.getHelper(ctx, DbSqlHelper.class);
        Dao<Medico, Long> medicoDao = databaseHelper.getDao(Medico.class);
        medicoDao.createOrUpdate((Medico) objeto);
        return true;
    }
}
