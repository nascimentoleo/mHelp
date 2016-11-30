package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Usuario;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 11/28/16.
 */
public class UsuariosController extends BaseController implements IController {

    public UsuariosController(Context ctx) {
        super(ctx);
    }

    public boolean loginExiste(String login,Dao<Usuario,Long> usuarioDao) throws SQLException {
        Dao<Usuario, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Usuario.class);
        List<Usuario> listaDeUsuarios =  usuarioDao.queryForMatchingArgs(new Usuario(login, ""));
        return !listaDeUsuarios.isEmpty();
    }

    @Override
    public boolean persistir(IModel objeto) throws SQLException {
        Dao<Usuario, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Usuario.class);
        if(!loginExiste(((Usuario) objeto).getLogin(), dao)) {
            dao.create((Usuario) objeto);
            return true;
        }else
            this.msgErro = "Usuário já existe no banco";
        return false;
    }


}
