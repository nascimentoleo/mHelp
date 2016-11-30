package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.Paciente;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by leo on 11/29/16.
 */
public class CadastroDePacientes extends ControleBase{

    public boolean persistir(Context ctx, Paciente paciente){
        DbSqlHelper databaseHelper = OpenHelperManager.getHelper(ctx, DbSqlHelper.class);
        try {
            Dao<Paciente,Long> pacienteDao = databaseHelper.getDao(Paciente.class);
            pacienteDao.createOrUpdate(paciente);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            this.msgErro = e.getMessage();
        }
        return false;
    }
}
