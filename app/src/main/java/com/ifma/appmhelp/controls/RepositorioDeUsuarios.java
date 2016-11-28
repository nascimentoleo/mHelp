package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.db.TablesSqlHelper;
import com.ifma.appmhelp.models.Usuario;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 11/28/16.
 */
public class RepositorioDeUsuarios {

    private String msgErro;
    private Dao<Usuario,Long> usuarioDao;

    public String getMsgErro() {
        return msgErro;
    }

    public boolean cadastrar(Context ctx, Usuario usuario){
        TablesSqlHelper databaseHelper = OpenHelperManager.getHelper(ctx, TablesSqlHelper.class);
        databaseHelper.setTableClass(Usuario.class);
        try {
            Dao<Usuario,Long> usuarioDao = databaseHelper.getDao(Usuario.class);
            if(!this.usuarioExiste(usuario, usuarioDao)) {
                usuarioDao.create(usuario);
                return true;
            }
            else
                this.msgErro = "Usuário já cadastrado";
        } catch (SQLException e) {
            e.printStackTrace();
            this.msgErro = e.getMessage();
        } finally{
            databaseHelper.close();
        }
        return false;
    }

    private boolean usuarioExiste(Usuario usuario,Dao<Usuario,Long> usuarioDao) throws SQLException {
        List<Usuario> listaDeUsuarios =  usuarioDao.queryForEq("login",usuario.getLogin());
        return listaDeUsuarios.size() > 0;
    }
}
