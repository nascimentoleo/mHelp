package com.ifma.appmhelp.processors;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.ifma.appmhelp.controls.OcorrenciasController;
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

/**
 * Created by leo on 12/20/16.
 */

public class ProcessadorDeSolicitacoes implements ProcessadorDeStanzas {

    @Override
    public void processar(Context ctx, Mensagem mensagem) throws Exception {
        SolicitacaoRoster solicitacaoRoster = SolicitacaoRoster.fromJson(mensagem.getMsg());
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(ctx);
        Intent it = new Intent(IntentType.SOLICITACAO_ROSTER.toString());

        if (solicitacaoRoster.getStatus() == StatusSolicitacaoRoster.ENVIADA) {
            it.putExtra(SolicitacaoBundleKeys.USUARIO_MEDICO.toString(), solicitacaoRoster.getUsuario());
            it.putExtra(SolicitacaoBundleKeys.FINALIZOU.toString(), false);

            //Remove o médico
        } else if (solicitacaoRoster.getStatus() == StatusSolicitacaoRoster.REMOVIDA) {
            //Essa rotina terá que ir para um serviço separado
            //Procuro o usuario
            Usuario usuario = (Usuario) solicitacaoRoster.getUsuario();
            usuario.setId(null);
            new UsuarioDao(ctx).carregaId(usuario);
            //Agora procuro o médico
            Medico medico = new MedicoDao(ctx).getMedicoByUsuario(usuario);
            if (medico != null){
                if (SolicitacoesController.removerUsuario(ctx, medico)) {
                    //Removo as ocorrências, mensagens e anexos
                    new OcorrenciasController(ctx).removerOcorrencias(medico);

                    LocalBroadcastManager.getInstance(ctx).sendBroadcast(new Intent(IntentType.ATUALIZAR_OCORRENCIAS.toString()));

                    Toast.makeText(ctx, "Você foi removido pelo médico " + usuario.getNome(), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            it.putExtra(SolicitacaoBundleKeys.FINALIZOU.toString(), true);
            if (solicitacaoRoster.getStatus() == StatusSolicitacaoRoster.APROVADA) {
                it.putExtra(SolicitacaoBundleKeys.ACEITOU_SOLICITACAO.toString(), true);
            } else
                it.putExtra(SolicitacaoBundleKeys.ACEITOU_SOLICITACAO.toString(), false);
        }

        lbm.sendBroadcast(it);
    }

}
