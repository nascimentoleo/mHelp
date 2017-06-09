package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.daos.PacienteDao;
import com.ifma.appmhelp.daos.UsuarioDao;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.PacienteParaEnvio;
import com.ifma.appmhelp.models.Prontuario;
import com.ifma.appmhelp.models.ProntuarioParaEnvio;
import com.ifma.appmhelp.models.Usuario;
import com.ifma.appmhelp.models.UsuarioLogado;

import java.sql.SQLException;

/**
 * Created by leo on 4/21/17.
 */

public class PacienteController extends BaseController {

    public PacienteController(Context ctx) {
        super(ctx);
    }

    public PacienteParaEnvio getPacienteParaEnvio(Paciente paciente) throws CloneNotSupportedException, SQLException {

        PacienteParaEnvio pacienteParaEnvio = new PacienteParaEnvio();

        pacienteParaEnvio.setPaciente(paciente.clone());
        Prontuario prontuario = pacienteParaEnvio.getPaciente().getProntuario();
        pacienteParaEnvio.getPaciente().setUsuario(paciente.getUsuario().clone());

        if (paciente.getProntuario() != null){
            ProntuarioParaEnvio prontuarioParaEnvio = new ProntuarioController(ctx).getProntuarioParaEnvio(prontuario);
            pacienteParaEnvio.setProntuarioParaEnvio(prontuarioParaEnvio);

        }

        pacienteParaEnvio.preparaPacienteParaEnvio();

        return pacienteParaEnvio;
    }

    public Paciente carregaPaciente(String login) throws SQLException {
        Paciente paciente;
        if (UsuarioLogado.getInstance().getModelo().getClass() == Paciente.class)
            paciente = (Paciente) UsuarioLogado.getInstance().getModelo();
        else {
            Usuario usuarioDB = new UsuarioDao(ctx).getUsuarioByLogin(login);
            PacienteDao pacienteDao = new PacienteDao(ctx);
            paciente = pacienteDao.getPacienteByUsuario(usuarioDB);
        }

        return paciente;

    }

}
