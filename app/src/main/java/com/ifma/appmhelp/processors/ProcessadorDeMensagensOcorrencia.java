package com.ifma.appmhelp.processors;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.ifma.appmhelp.controls.MensagemController;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.enums.IntentType;
import com.ifma.appmhelp.models.Mensagem;

/**
 * Created by leo on 3/9/17.
 */
public class ProcessadorDeMensagensOcorrencia implements ProcessadorDeStanzas {

    @Override
    public void processar(Context ctx, Mensagem mensagem) throws Exception {

        new MensagemController(ctx).salvarMensagem(mensagem);
        //Atualiza a lista de mensagens
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(ctx);
        Intent it = new Intent(IntentType.ATUALIZAR_MENSAGENS.toString());
        it.putExtra(GenericBundleKeys.MENSAGEM.toString(), mensagem);
        lbm.sendBroadcast(it);
        //Notifica o aparelho
        Intent itn = new Intent(IntentType.NOTIFICAR_MENSAGEM.toString());
        itn.putExtra(GenericBundleKeys.MENSAGEM.toString(), mensagem);
        lbm.sendBroadcast(itn);
        //Atualiza a lista de ocorrencias
        Intent ito = new Intent(IntentType.ATUALIZAR_OCORRENCIAS.toString());
        ito.putExtra(GenericBundleKeys.OCORRENCIA.toString(), mensagem.getOcorrencia());
        ito.putExtra(GenericBundleKeys.MENSAGEM.toString(), mensagem);
        lbm.sendBroadcast(ito);
    }
}
