package com.ifma.appmhelp.processors;

import android.content.Context;

import com.ifma.appmhelp.daos.PacienteDao;
import com.ifma.appmhelp.daos.ProntuarioCidDao;
import com.ifma.appmhelp.daos.ProntuarioDao;
import com.ifma.appmhelp.daos.ProntuarioMedicamentoDao;
import com.ifma.appmhelp.daos.UsuarioDao;
import com.ifma.appmhelp.models.Cid;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.ProntuarioCid;
import com.ifma.appmhelp.models.ProntuarioMedicamento;
import com.ifma.appmhelp.models.ProntuarioParaEnvio;
import com.ifma.appmhelp.models.Usuario;
import com.ifma.appmhelp.models.UsuarioLogado;

import java.sql.SQLException;

/**
 * Created by leo on 1/11/17.
 */

public class ProcessadorDeProntuarios implements ProcessadorDeStanzas {

    @Override
    public void processar(Context ctx, Mensagem mensagem) throws Exception {
        ProntuarioParaEnvio prontuarioParaEnvio = ProntuarioParaEnvio.fromJson(mensagem.getMsg());

        //Carrego usuário de acordo com o usuario Logado que está processando o Prontuario
        Paciente paciente;
        if (UsuarioLogado.getInstance().getModelo().getClass() == Paciente.class)
            paciente = (Paciente) UsuarioLogado.getInstance().getModelo();
        else {
            Usuario usuario = new UsuarioDao(ctx).getUsuarioByLogin(prontuarioParaEnvio.getUsuario().getLogin());
            paciente = new PacienteDao(ctx).getPacienteByUsuario(usuario);

        }
        if (paciente != null)
            this.atualizarProntuario(ctx, prontuarioParaEnvio, paciente);
    }

    private void atualizarProntuario(Context ctx, ProntuarioParaEnvio prontuarioParaEnvio, Paciente paciente) throws SQLException {

        if (paciente.getProntuario() == null)
            prontuarioParaEnvio.getProntuario().setId(null);
        else
            prontuarioParaEnvio.getProntuario().setId(paciente.getProntuario().getId());

        new ProntuarioDao(ctx).persistir(prontuarioParaEnvio.getProntuario(), true);
        paciente.setProntuario(prontuarioParaEnvio.getProntuario());
        new PacienteDao(ctx).persistir(paciente, false);

        //Adiciono cids
        ProntuarioCidDao prontuarioCidDao = new ProntuarioCidDao(ctx);
        prontuarioCidDao.removerTodos(paciente.getProntuario());
        for (Cid cid : prontuarioParaEnvio.getCids()){
            prontuarioCidDao.persistir(new ProntuarioCid(paciente.getProntuario(), cid), false);
        }

        //Adiciono medicamentos
        ProntuarioMedicamentoDao prontuarioMedicamentoDao = new ProntuarioMedicamentoDao(ctx);
        prontuarioMedicamentoDao.removerTodos(paciente.getProntuario());

        for (ProntuarioParaEnvio.MedicamentoParaEnvio medicamento : prontuarioParaEnvio.getMedicamentos()){
            prontuarioMedicamentoDao.persistir(new ProntuarioMedicamento(paciente.getProntuario(),
                    medicamento.getMedicamento(), medicamento.getDoses()), false);
        }

    }
}
