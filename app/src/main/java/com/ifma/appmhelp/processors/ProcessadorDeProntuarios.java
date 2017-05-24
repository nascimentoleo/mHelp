package com.ifma.appmhelp.processors;

import android.content.Context;

import com.ifma.appmhelp.controls.PacienteController;
import com.ifma.appmhelp.controls.ProntuarioController;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.ProntuarioParaEnvio;

/**
 * Created by leo on 1/11/17.
 */

public class ProcessadorDeProntuarios implements ProcessadorDeStanzas {

    @Override
    public void processar(Context ctx, Mensagem mensagem) throws Exception {
        //Extrai os dados do prontuário contidos no corpo da mensgem
        ProntuarioParaEnvio prontuarioParaEnvio = ProntuarioParaEnvio.fromJson(mensagem.getMsg());
        //Carrega dados do paciente a quem pertence o prontuário
        Paciente paciente = new PacienteController(ctx).carregaPaciente(prontuarioParaEnvio.getUsuario().getLogin());
        //Persiste os dados no banco
        if (paciente != null)
            new ProntuarioController(ctx).atualizarProntuario(prontuarioParaEnvio, paciente);

    }

}
