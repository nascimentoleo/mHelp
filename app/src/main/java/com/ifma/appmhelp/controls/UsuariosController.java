package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Usuario;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 11/28/16.
 */
public class UsuariosController extends BaseController {

    public UsuariosController(Context ctx) {
        super(ctx);
    }

    private boolean loginExiste(String login,Dao<Usuario,Long> usuarioDao) throws SQLException {
        List<Usuario> listaDeUsuarios =  usuarioDao.queryForMatchingArgs(new Usuario(login, ""));
        return !listaDeUsuarios.isEmpty();
    }

    @Override
    public boolean persistir(IModel objeto) throws SQLException {
        DbSqlHelper databaseHelper = OpenHelperManager.getHelper(ctx, DbSqlHelper.class);
        Dao<Usuario,Long> usuarioDao = databaseHelper.getDao(Usuario.class);
        if(!loginExiste(((Usuario) objeto).getLogin(), usuarioDao)) {
            usuarioDao.create((Usuario) objeto);
            return true;
        }else
            this.msgErro = "Usuário já existe no banco";
        return false;
    }
}
