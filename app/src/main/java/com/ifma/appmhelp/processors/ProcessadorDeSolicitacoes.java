package com.ifma.appmhelp.processors;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.ifma.appmhelp.controls.OcorrenciaController;
import com.ifma.appmhelp.controls.SolicitacoesController;
import com.ifma.appmhelp.daos.MedicoDao;
import com.ifma.appmhelp.daos.UsuarioDao;
import com.ifma.appmhelp.enums.IntentType;
import com.ifma.appmhelp.enums.SolicitacaoBundleKeys;
import com.ifma.appmhelp.enums.StatusSolicitacaoRoster;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.SolicitacaoRoster;
import com.ifma.appmhelp.models.Usuario;
import com.ifma.appmhelp.services.MensagemNotification;

/**
 * Created by leo on 12/20/16.
 */

public class ProcessadorDeSolicitacoes implements ProcessadorDeStanzas {

    @Override
    public void processar(Context ctx, Mensagem mensagem) throws Exception {
        SolicitacaoRoster solicitacaoRoster = SolicitacaoRoster.fromJson(mensagem.getMsg());
        Intent it = new Intent(IntentType.SOLICITACAO_ROSTER.toString());

        if (solicitacaoRoster.getStatus() == StatusSolicitacaoRoster.ENVIADA)
            it = this.solicitacaoEnviada(solicitacaoRoster, it);
        else if (solicitacaoRoster.getStatus() == StatusSolicitacaoRoster.REMOVIDA)
            it = this.solicitacaoRemovida(ctx, solicitacaoRoster, it);
        else
            it = this.solicitacaoFinalizada(solicitacaoRoster, it);

        LocalBroadcastManager.getInstance(ctx).sendBroadcast(it);
    }

    private Intent solicitacaoEnviada(SolicitacaoRoster solicitacao, Intent it){
        it.putExtra(SolicitacaoBundleKeys.USUARIO_MEDICO.toString(), solicitacao.getUsuario());
        it.putExtra(SolicitacaoBundleKeys.FINALIZOU.toString(), false);
        return it;
    }

    private Intent solicitacaoRemovida(Context ctx, SolicitacaoRoster solicitacao, Intent it) throws Exception {
        //Procuro o usuario
        Usuario usuario = (Usuario) solicitacao.getUsuario();
        usuario.setId(null);
        new UsuarioDao(ctx).carregaId(usuario);
        //Agora procuro o médico
        Medico medico = new MedicoDao(ctx).getMedicoByUsuario(usuario);
        if (medico != null) {
            if (SolicitacoesController.removerUsuario(ctx, medico)) {
                //Removo as ocorrências, mensagens e anexos
                new OcorrenciaController(ctx).removerOcorrencias(medico);

                LocalBroadcastManager.getInstance(ctx).sendBroadcast(new Intent(IntentType.ATUALIZAR_OCORRENCIAS.toString()));
                MensagemNotification.notify(ctx,"Removido",  "Você foi removido ", " pelo médico " + usuario.getNome(),"", null);
            }
        }
        return it;
    }

    private Intent solicitacaoFinalizada(SolicitacaoRoster solicitacao, Intent it){
        it.putExtra(SolicitacaoBundleKeys.FINALIZOU.toString(), true);
        if (solicitacao.getStatus() == StatusSolicitacaoRoster.APROVADA) {
            it.putExtra(SolicitacaoBundleKeys.ACEITOU_SOLICITACAO.toString(), true);
        } else
            it.putExtra(SolicitacaoBundleKeys.ACEITOU_SOLICITACAO.toString(), false);
        return it;
    }


}
