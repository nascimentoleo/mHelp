package com.ifma.appmhelp.processors;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.ifma.appmhelp.controls.OcorrenciaController;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.enums.IntentType;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Ocorrencia;

/**
 * Created by leo on 2/27/17.
 */
public class ProcessadorDeOcorrencias implements ProcessadorDeStanzas {

    @Override
    public void processar(Context ctx, Mensagem mensagem) throws Exception {
        //Extrai a ocorrência e persiste no banco
        Ocorrencia ocorrencia = Ocorrencia.fromJson(mensagem.getMsg());
        new OcorrenciaController(ctx).adicionarOcorrenciaFromPaciente(ocorrencia);

        //Envia broadcast notificando da nova ocorrência
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(ctx);
        Intent it = new Intent(IntentType.ATUALIZAR_OCORRENCIAS.toString());
        it.putExtra(GenericBundleKeys.OCORRENCIA.toString(),ocorrencia);
        lbm.sendBroadcast(it);

    }
}
