package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.models.Mensagem;

/**
 * Created by leo on 1/11/17.
 */

public class ProcessadorDeProntuarios implements ProcessadorDeMensagens {

    @Override
    public void processar(Context ctx, Mensagem mensagem) throws Exception {
        /* Prontuario prontuario = Prontuario.fromJson(mensagem.getMsg());
        Paciente paciente = (Paciente) UsuarioLogado.getInstance().getModelo();
        //Limpo Id
        prontuario.setId(null);
        new ProntuarioDao(ctx).persistir(prontuario, true);
        paciente.setProntuario(prontuario);
        new PacienteDao(ctx).persistir(paciente, false);
        */
        
    }
}
