package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Ocorrencia;

/**
 * Created by leo on 2/27/17.
 */
public class ProcessadorDeOcorrencias implements ProcessadorDeMensagens {

    @Override
    public void processar(Context ctx, Mensagem mensagem) throws Exception {
        Ocorrencia ocorrencia = Ocorrencia.fromJson(mensagem.getMsg());
        new OcorrenciasController(ctx).adicionarOcorrenciaFromPaciente(ocorrencia);
        //Aqui vai lançar um broadcast para atualizar o adapter das ocorrências

    }
}
