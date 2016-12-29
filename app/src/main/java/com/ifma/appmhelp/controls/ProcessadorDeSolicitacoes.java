package com.ifma.appmhelp.controls;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.ifma.appmhelp.daos.MedicosDao;
import com.ifma.appmhelp.daos.UsuariosDao;
import com.ifma.appmhelp.enums.StatusSolicitacaoRoster;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.SolicitacaoRoster;
import com.ifma.appmhelp.models.Usuario;

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

            //Remove o médico
        } else if (solicitacaoRoster.getStatus() == StatusSolicitacaoRoster.REMOVIDA) {
            //Essa rotina terá que ir para um serviço separado
            //Procuro o usuario
            Usuario usuario = (Usuario) solicitacaoRoster.getUsuario();
            usuario.setId(null);
            new UsuariosDao(ctx).carregaId(usuario);
            //Agora procuro o médico
            Medico medico = new MedicosDao(ctx).getMedicoByUsuario(usuario);
            if (medico != null){
                if (SolicitacoesController.removerUsuario(ctx, medico))
                    Toast.makeText(ctx, "Você foi removido pelo médico " + usuario.getNome(), Toast.LENGTH_SHORT).show();
            }
        } else {
            it.putExtra("finalizou", true);
            if (solicitacaoRoster.getStatus() == StatusSolicitacaoRoster.APROVADA) {
                it.putExtra("aceitou_solicitacao", true);
            } else
                it.putExtra("aceitou_solicitacao", false);
        }

        lbm.sendBroadcast(it);
    }

}
