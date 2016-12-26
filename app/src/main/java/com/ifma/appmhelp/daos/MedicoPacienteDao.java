package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.MedicoPaciente;
import com.ifma.appmhelp.models.Paciente;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 12/20/16.
 */

public class MedicoPacienteDao extends BaseController implements IDao {

    public MedicoPacienteDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(IModel objeto, boolean updateChild) throws SQLException {
        MedicoPaciente medicoPaciente = (MedicoPaciente) objeto;
        if (updateChild){
           new MedicosDao(ctx).persistir(medicoPaciente.getMedico(),updateChild);
           new PacientesDao(ctx).persistir(medicoPaciente.getPaciente(),updateChild);
        }

        Dao<MedicoPaciente, Long> dao = DbSqlHelper.getHelper(ctx).getDao(MedicoPaciente.class);
        dao.createOrUpdate(medicoPaciente);
        return true;
    }

    @Override
    public void carregaId(IModel objeto) throws SQLException {

    }

    public List<Paciente> getPacientesByMedico(Medico medico) throws SQLException {
        Dao<MedicoPaciente, Long> dao = DbSqlHelper.getHelper(ctx).getDao(MedicoPaciente.class);
        List<MedicoPaciente> medicoPacienteList = dao.queryForEq("medico_id", medico.getId());
        if (!medicoPacienteList.isEmpty()){
            List<Paciente> pacientes = new ArrayList<>();
            for(MedicoPaciente medicoPaciente : medicoPacienteList){
                pacientes.add(medicoPaciente.getPaciente());
            }
            return pacientes;
        }
        return null;

    }

}
