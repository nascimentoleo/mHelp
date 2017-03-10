package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
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

public class MedicoPacienteDao extends BaseController implements IDao<MedicoPaciente> {

    public MedicoPacienteDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(MedicoPaciente objeto, boolean updateChild) throws SQLException {
        if (updateChild){
           new MedicoDao(ctx).persistir(objeto.getMedico(),updateChild);
           new PacienteDao(ctx).persistir(objeto.getPaciente(),updateChild);
        }

        Dao<MedicoPaciente, Long> dao = DbSqlHelper.getHelper(ctx).getDao(MedicoPaciente.class);
        dao.createOrUpdate(objeto);
        return true;
    }

    @Override
    public void carregaId(MedicoPaciente objeto) throws SQLException {
        Dao<MedicoPaciente, Long> dao = DbSqlHelper.getHelper(ctx).getDao(MedicoPaciente.class);
        List<MedicoPaciente> medicoPacientes = dao.queryForMatching(objeto);
        if(!medicoPacientes.isEmpty())
            objeto.setId(medicoPacientes.get(0).getId());
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

    public List<Medico> getMedicosByPaciente(Paciente paciente)throws SQLException{
        Dao<MedicoPaciente, Long> dao = DbSqlHelper.getHelper(ctx).getDao(MedicoPaciente.class);
        List<MedicoPaciente> medicoPacienteList = dao.queryForEq("paciente_id", paciente.getId());
        if (!medicoPacienteList.isEmpty()){
            List<Medico> medicos = new ArrayList<>();
            for(MedicoPaciente medicoPaciente : medicoPacienteList){
                medicos.add(medicoPaciente.getMedico());
            }
            return medicos;
        }
        return null;
    }

    @Override
    public void remover(MedicoPaciente objeto, boolean updateChild) throws SQLException {
        if (updateChild){
            new MedicoDao(ctx).remover(objeto.getMedico(),updateChild);
            new PacienteDao(ctx).remover(objeto.getPaciente(),updateChild);
        }

        Dao<MedicoPaciente, Long> dao = DbSqlHelper.getHelper(ctx).getDao(MedicoPaciente.class);
        dao.delete(objeto);
    }

}
