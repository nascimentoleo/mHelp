package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.daos.MedicoPacienteDao;
import com.ifma.appmhelp.daos.MedicoDao;
import com.ifma.appmhelp.daos.PacienteDao;
import com.ifma.appmhelp.daos.UsuarioDao;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.MedicoPaciente;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Usuario;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.sql.SQLException;

/**
 * Created by leo on 12/28/16.
 */

public class SolicitacoesController {

    public static boolean adicionarUsuario(Context ctx, IModel modelOwner, IModel modelOwned) throws SQLException, SmackException.NotLoggedInException, XMPPException.XMPPErrorException, SmackException.NotConnectedException, SmackException.NoResponseException {
        if (modelOwner.getClass() == Medico.class)
            return adicionarPaciente(ctx, (Medico) modelOwner, (Paciente) modelOwned);
        else
            return adicionarMedico(ctx, (Medico) modelOwned, (Paciente) modelOwner);
    }

    private static boolean adicionarPaciente(Context ctx, Medico medico, Paciente paciente) throws SmackException.NotLoggedInException, XMPPException.XMPPErrorException, SmackException.NotConnectedException, SmackException.NoResponseException, SQLException {
        adicionarRoster(paciente.getUsuario());
        PacienteDao pacienteDao = new PacienteDao(ctx);
        UsuarioDao usuarioDao = new UsuarioDao(ctx);

        //Verifico se esse usuário já foi adicionado anteriormente
        Usuario usuarioDB = usuarioDao.getUsuarioByLogin(paciente.getUsuario().getLogin());
        if (usuarioDB == null) {
            //Novos ids serão criados
            paciente.setId(null);
            paciente.getUsuario().setId(null);
        } else {
            //Pego ids existentes
            paciente.getUsuario().setId(usuarioDB.getId());
            Paciente pacienteDB = pacienteDao.getPacienteByUsuario(usuarioDB);

            if (pacienteDB != null) {
                paciente.setId(pacienteDB.getId());

                if (paciente.getProntuario() == null)
                    paciente.setProntuario(pacienteDB.getProntuario());
            }
        }
        MedicoPaciente medicoPaciente = new MedicoPaciente(medico, paciente);
        MedicoPacienteDao medicoPacienteDao = new MedicoPacienteDao(ctx);

        pacienteDao.persistir(paciente,true);
        medicoPacienteDao.carregaId(medicoPaciente);
        medicoPacienteDao.persistir(medicoPaciente, false);
        return true;
    }

    private static boolean adicionarMedico(Context ctx, Medico medico, Paciente paciente) throws SQLException, SmackException.NotLoggedInException, XMPPException.XMPPErrorException, SmackException.NotConnectedException, SmackException.NoResponseException {
        adicionarRoster(medico.getUsuario());
        UsuarioDao usuarioDao = new UsuarioDao(ctx);
        MedicoDao medicoDao = new MedicoDao(ctx);
        Usuario usuarioDB = usuarioDao.getUsuarioByLogin(medico.getUsuario().getLogin());

        if (usuarioDB == null){
            medico.setId(null);
            medico.getUsuario().setId(null);
        }else{
            medico.getUsuario().setId(usuarioDB.getId());

            Medico medicoDB = medicoDao.getMedicoByUsuario(usuarioDB);
            if(medicoDB != null){
                medico.setId(medicoDB.getId());
            }

        }
        MedicoPaciente medicoPaciente = new MedicoPaciente(medico, paciente);
        MedicoPacienteDao medicoPacienteDao = new MedicoPacienteDao(ctx);
        //Adiciono primeiro o médico em cascata
        medicoDao.persistir(medico, true);

        medicoPacienteDao.carregaId(medicoPaciente);
        medicoPacienteDao.persistir(medicoPaciente,false);
        return true;
    }



    public static boolean removerUsuario(Context ctx, IModel modelo) throws Exception {
        if (modelo.getClass() == Paciente.class)
            return removerPaciente(ctx,(Paciente) modelo);
        return removerMedico(ctx,(Medico) modelo);
    }

    private static boolean removerPaciente(Context ctx, Paciente paciente) throws Exception {
        if (removerRoster(paciente.getUsuario())){
            MedicoPaciente medicoPaciente = new MedicoPaciente(null, paciente);
            MedicoPacienteDao dao = new MedicoPacienteDao(ctx);
            dao.carregaId(medicoPaciente);
            //Não removo em cascata para que não haja a exclusão do médico
            dao.remover(medicoPaciente, false);
            new PacienteDao(ctx).remover(paciente, true);
            return true;
        }
        return false;
    }

    private static boolean removerMedico(Context ctx, Medico medico) throws Exception {
        if (removerRoster(medico.getUsuario())){
            MedicoPaciente medicoPaciente = new MedicoPaciente(medico, null);
            MedicoPacienteDao dao = new MedicoPacienteDao(ctx);
            dao.carregaId(medicoPaciente);
            dao.remover(medicoPaciente, false);
            new MedicoDao(ctx).remover(medico, true);
            return true;
        }
        return false;
    }

    private static boolean removerRoster(Usuario usuario) throws Exception {
        RosterXMPPController.getInstance().removeRoster(usuario);
        return true;
    }

    private static boolean adicionarRoster(Usuario usuario) throws SmackException.NotLoggedInException, XMPPException.XMPPErrorException, SmackException.NotConnectedException, SmackException.NoResponseException {
        RosterXMPPController.getInstance().addRoster(usuario);
        return true;
    }

}
