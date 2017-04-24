package com.ifma.appmhelp.services;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.enums.IntentType;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Ocorrencia;
import com.ifma.appmhelp.models.Usuario;
import com.ifma.appmhelp.views.MensagensActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by leo on 3/23/17.
 */

public class NotificationListener {

    private List<Mensagem> mensagensNotificadas;
    private List<Usuario> usuariosRemetentes;
    private Context ctx;

    private BroadcastReceiver mReceiverNotificar = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Mensagem mensagem = (Mensagem) intent.getSerializableExtra(GenericBundleKeys.MENSAGEM.toString());
            if (adicionarMensagem(mensagem))
                notificar();
        }
    };

    private BroadcastReceiver mReceiverLimpar = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Ocorrencia ocorrencia = (Ocorrencia) intent.getSerializableExtra(GenericBundleKeys.OCORRENCIA.toString());
            limparMensagens(ocorrencia);
            MensagemNotification.cancel(ctx);

        }
    };

    private void notificar() {
        Mensagem ultimaMensagem   = mensagensNotificadas.get(mensagensNotificadas.size() -1);
        boolean existeMaisDeUmRemetente = usuariosRemetentes.size() > 1;
        boolean existeMaisDeUmaMensagem = mensagensNotificadas.size() > 1;
        PendingIntent pendingIntent = this.getMensagemIntent(ultimaMensagem.getOcorrencia());

        String ticker = "Nova Mensagem";
        String title;
        String text = "";

        if (existeMaisDeUmaMensagem)
            text =  mensagensNotificadas.size() + " mensagens";
        else if (ultimaMensagem.getMsg() != null)
            text = ultimaMensagem.getMsg();
        else if (ultimaMensagem.getAnexo() != null){
            text = ultimaMensagem.getAnexo().getTipoAnexo().toString();
        }

        if (existeMaisDeUmRemetente) {
            title = ctx.getString(R.string.app_name);
            text += " de " + usuariosRemetentes.size() + " usuários";
        }else
            title = ultimaMensagem.getUsuario().getNome();

        String bigText = "";

        for (Mensagem mensagem : mensagensNotificadas){
            if (mensagem.getMsg() != null)
                bigText += mensagem.getMsg() + "\n";
        }

        MensagemNotification.notify(ctx, ticker, title, text, bigText, pendingIntent);
    }


    public NotificationListener(Context ctx) {
        this.ctx = ctx;
        mensagensNotificadas = new ArrayList<>();
        usuariosRemetentes = new ArrayList<>();
        LocalBroadcastManager.getInstance(ctx).registerReceiver(mReceiverNotificar, new IntentFilter(IntentType.NOTIFICAR_MENSAGEM.toString()));
        LocalBroadcastManager.getInstance(ctx).registerReceiver(mReceiverLimpar, new IntentFilter(IntentType.LIMPAR_MENSAGEM.toString()));
    }

    private PendingIntent getMensagemIntent(Ocorrencia ocorrencia){
        Intent mensagemIntent = new Intent(ctx, MensagensActivity.class);
        mensagemIntent.putExtra(GenericBundleKeys.OCORRENCIA.toString(),ocorrencia);
        return PendingIntent.getActivity(ctx, 0,
                mensagemIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void limparMensagens(Ocorrencia ocorrencia) {
        Iterator<Mensagem> iter = mensagensNotificadas.iterator();

        while (iter.hasNext()) {
            Mensagem mensagem = iter.next();

            if (ocorrencia.equals(mensagem.getOcorrencia())) {
                usuariosRemetentes.remove(mensagem.getUsuario());
                iter.remove();
            }

        }
    }

    private boolean adicionarMensagem(Mensagem mensagem){
        if (!MensagensActivity.isActive() || !mensagem.getOcorrencia().equals(MensagensActivity.getOcorrencia())) {
            if (!mensagensNotificadas.contains(mensagem)) {
                mensagensNotificadas.add(mensagem);
                if (!usuariosRemetentes.contains(mensagem.getUsuario()))
                    usuariosRemetentes.add(mensagem.getUsuario());

                return true;
            }
        }
        return false;
    }
}
