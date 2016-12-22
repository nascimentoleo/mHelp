package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Usuario;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by leo on 11/29/16.
 */
public class MedicosController extends BaseController implements IController{

    public MedicosController(Context ctx) {
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
    public boolean persistir(IModel objeto, boolean updateChild) throws SQLException {
        Medico medico = (Medico) objeto;
        if (updateChild)
            new UsuariosController(ctx).persistir(medico.getUsuario(), updateChild);

        Dao<Medico, Long> medicoDao = DbSqlHelper.getHelper(ctx).getDao(Medico.class);
        medicoDao.createOrUpdate(medico);
        return true;
    }

    @Override
    public void carregaId(IModel objeto) throws SQLException {
        Dao<Medico, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Medico.class);
        List<Medico> medicos = dao.queryForMatching((Medico) objeto);
        if(!medicos.isEmpty())
            objeto.setId(medicos.get(0).getId());
    }


}
