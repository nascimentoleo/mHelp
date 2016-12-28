package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
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
public class PacientesDao extends BaseController implements IDao {

    public PacientesDao(Context ctx) {
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
    public boolean persistir(IModel objeto, boolean updateChild) throws SQLException {
        Paciente paciente = (Paciente) objeto;
        if (updateChild) {
            new UsuariosDao(ctx).persistir(paciente.getUsuario(), updateChild);
            new ProntuarioDao(ctx).persistir(paciente.getProntuario(), updateChild);
        }
        Dao<Paciente,Long> pacienteDao = DbSqlHelper.getHelper(ctx).getDao(Paciente.class);
        pacienteDao.createOrUpdate(paciente);
        return true;
    }

    @Override
    public void remover(IModel objeto, boolean updateChild) throws SQLException {
        Paciente paciente = (Paciente) objeto;
        if (updateChild) {
            new UsuariosDao(ctx).remover(paciente.getUsuario(), updateChild);
            new ProntuarioDao(ctx).remover(paciente.getProntuario(), updateChild);
        }
        Dao<Paciente,Long> pacienteDao = DbSqlHelper.getHelper(ctx).getDao(Paciente.class);
        pacienteDao.createOrUpdate(paciente);
    }

    @Override
    public void carregaId(IModel objeto) throws SQLException {

    }

    public Paciente getPacienteById(Long id) throws SQLException {
        Dao<Paciente, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Paciente.class);
        return dao.queryForId(id);
    }


}
