package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Usuario;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 11/29/16.
 */
public class PacienteDao extends BaseController implements IDao<Paciente> {

    public PacienteDao(Context ctx) {
        super(ctx);
    }

    public Paciente getPacienteByUsuario(Usuario usuario) throws SQLException {
        Dao<Paciente, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Paciente.class);
        List<Paciente> pacientes = dao.queryForMatching(new Paciente(usuario));
        if (!pacientes.isEmpty())
           return pacientes.get(0);
        return null;

    }

    @Override
    public boolean persistir(Paciente objeto, boolean updateChild) throws SQLException {
        if (updateChild) {
            new UsuarioDao(ctx).persistir(objeto.getUsuario(), updateChild);
            new ProntuarioDao(ctx).persistir(objeto.getProntuario(), updateChild);
        }
        Dao<Paciente,Long> pacienteDao = DbSqlHelper.getHelper(ctx).getDao(Paciente.class);
        pacienteDao.createOrUpdate(objeto);
        return true;
    }

    @Override
    public void remover(Paciente objeto, boolean updateChild) throws SQLException {
        if (updateChild) {
            new UsuarioDao(ctx).remover(objeto.getUsuario(), updateChild);
            new ProntuarioDao(ctx).remover(objeto.getProntuario(), updateChild);
        }
        Dao<Paciente,Long> pacienteDao = DbSqlHelper.getHelper(ctx).getDao(Paciente.class);
        pacienteDao.delete(objeto);
    }

    @Override
    public void carregaId(Paciente objeto) throws SQLException {

    }

    public Paciente getPacienteById(Long id) throws SQLException {
        Dao<Paciente, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Paciente.class);
        return dao.queryForId(id);
    }


}
