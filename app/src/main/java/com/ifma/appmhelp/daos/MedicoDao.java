package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Usuario;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by leo on 11/29/16.
 */
public class MedicoDao extends BaseController implements IDao<Medico> {

    public MedicoDao(Context ctx) {
        super(ctx);
    }

    public Medico getMedicoByUsuario(Usuario usuario) throws SQLException {
        Dao<Medico, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Medico.class);
        List<Medico> medicos = dao.queryForMatching(new Medico(usuario));
        if (!medicos.isEmpty())
            return medicos.get(0);
        return null;
    }

    @Override
    public boolean persistir(Medico objeto, boolean updateChild) throws SQLException {
        if (updateChild)
            new UsuarioDao(ctx).persistir(objeto.getUsuario(), updateChild);

        Dao<Medico, Long> medicoDao = DbSqlHelper.getHelper(ctx).getDao(Medico.class);
        medicoDao.createOrUpdate(objeto);
        return true;
    }

    @Override
    public void remover(Medico objeto, boolean updateChild) throws SQLException {
        if (updateChild)
            new UsuarioDao(ctx).remover(objeto.getUsuario(), updateChild);

        Dao<Medico, Long> medicoDao = DbSqlHelper.getHelper(ctx).getDao(Medico.class);
        medicoDao.delete(objeto);
    }

    @Override
    public void carregaId(Medico objeto) throws SQLException {
        Dao<Medico, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Medico.class);
        List<Medico> medicos = dao.queryForMatching(objeto);
        if(!medicos.isEmpty())
            objeto.setId(medicos.get(0).getId());
    }


}
