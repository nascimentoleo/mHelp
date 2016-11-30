package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.Medico;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;


/**
 * Created by leo on 11/29/16.
 */
public class CadastroDeMedicos extends ControleBase {

    public boolean persistir(Context ctx, Medico medico){
        DbSqlHelper databaseHelper = OpenHelperManager.getHelper(ctx, DbSqlHelper.class);
        try {
            Dao<Medico,Long> medicoDao = databaseHelper.getDao(Medico.class);
            medicoDao.createOrUpdate(medico);
            return true;
            } catch (SQLException e) {
            e.printStackTrace();
            this.msgErro = e.getMessage();
        }
        return false;
    }
}
