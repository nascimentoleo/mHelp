package com.ifma.appmhelp.controls;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.ifma.appmhelp.enums.StatusSolicitacaoRoster;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.SolicitacaoRoster;

/**
 * Created by leo on 12/20/16.
 */

public class ProcessadorDeSolicitacoes implements ProcessadorDeMensagens {

    @Override
    public void processar(Context ctx, Mensagem mensagem) throws Exception {
        SolicitacaoRoster solicitacaoRoster = SolicitacaoRoster.fromJson(mensagem.getMsg());
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(ctx);
        Intent it = new Intent("solicitacao_roster");

        if (solicitacaoRoster.getStatus() == StatusSolicitacaoRoster.ENVIADA) {
            it.putExtra("usuario_medico", solicitacaoRoster.getUsuario());
            it.putExtra("finalizou", false);
        } else{
            it.putExtra("finalizou", true);
            if (solicitacaoRoster.getStatus() == StatusSolicitacaoRoster.APROVADA) {
                it.putExtra("aceitou_solicitacao", true);
            } else
                it.putExtra("aceitou_solicitacao", false);
        }

        lbm.sendBroadcast(it);
    }

}
