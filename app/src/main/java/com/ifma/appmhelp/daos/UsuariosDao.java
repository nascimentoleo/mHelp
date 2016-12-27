package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Usuario;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 11/28/16.
 */
public class UsuariosDao extends BaseController implements IDao {

    public UsuariosDao(Context ctx) {
        super(ctx);
    }

    public Usuario getUsuarioByLogin(String login) throws SQLException {
        Dao<Usuario, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Usuario.class);
        List<Usuario> listaDeUsuarios =  dao.queryForMatching(new Usuario(login));
        if (!listaDeUsuarios.isEmpty())
            return listaDeUsuarios.get(0);
        return null;
    }

    @Override
    public boolean persistir(IModel objeto, boolean updateChild) throws SQLException {
        Dao<Usuario, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Usuario.class);
        dao.createOrUpdate((Usuario) objeto);
        return true;
    }

    @Override
    public void carregaId(IModel objeto) throws SQLException {
        Dao<Usuario, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Usuario.class);
        List<Usuario> usuarios = dao.queryForMatching((Usuario) objeto);
        if(!usuarios.isEmpty())
            objeto.setId(usuarios.get(0).getId());
    }

    public void deletar(IModel objeto) throws SQLException {
        Dao<Usuario, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Usuario.class);
        dao.delete((Usuario) objeto);
    }
}
