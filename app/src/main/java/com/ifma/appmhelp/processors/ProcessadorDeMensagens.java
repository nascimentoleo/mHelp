package com.ifma.appmhelp.processors;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.ifma.appmhelp.daos.MensagemDao;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.enums.IntentType;
import com.ifma.appmhelp.models.Mensagem;

/**
 * Created by leo on 3/9/17.
 */
public class ProcessadorDeMensagens implements ProcessadorDeStanzas {

    @Override
    public void processar(Context ctx, Mensagem mensagem) throws Exception {


        new MensagemDao(ctx).persistir(mensagem, false);

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(ctx);
        Intent it = new Intent(IntentType.ATUALIZAR_MENSAGENS.toString());
        it.putExtra(GenericBundleKeys.MENSAGEM.toString(), mensagem);
        lbm.sendBroadcast(it);
    }
}
