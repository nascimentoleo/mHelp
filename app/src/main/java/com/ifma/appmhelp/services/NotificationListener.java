package com.ifma.appmhelp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.enums.IntentType;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 3/23/17.
 */

public class NotificationListener {

    private List<Mensagem> mensagensNotificadas;
    private List<Usuario> usuariosRemetentes;
    private Context ctx;


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Mensagem mensagem = (Mensagem) intent.getSerializableExtra(GenericBundleKeys.MENSAGEM.toString());
            if (!mensagensNotificadas.contains(mensagem)) {
                mensagensNotificadas.add(mensagem);
                if (!usuariosRemetentes.contains(mensagem.getUsuario()))
                    usuariosRemetentes.add(mensagem.getUsuario());
                notificar();
            }
        }
    };

    private void notificar() {
        Mensagem ultimaMensagem   = mensagensNotificadas.get(mensagensNotificadas.size() -1);
        boolean existeMaisDeUmRemetente = usuariosRemetentes.size() > 1;
        boolean existeMaisDeUmaMensagem = mensagensNotificadas.size() > 1;
        String ticker = "Nova Mensagem";
        String title;
        String text;

        if (existeMaisDeUmaMensagem)
            text =  mensagensNotificadas.size() + " mensagens";
        else
            text = ultimaMensagem.getMsg();

        if (existeMaisDeUmRemetente) {
            title = ctx.getString(R.string.app_name);
            text += " de " + usuariosRemetentes.size() + " usuários";
        }else
            title = ultimaMensagem.getUsuario().getNome();

        String bigText = "";

        for (Mensagem mensagem : mensagensNotificadas){
            bigText += mensagem.getMsg() + "\n";
        }

        MensagemNotification.notify(ctx, ticker, title, text, bigText, mensagensNotificadas.size());
    }

    public NotificationListener(Context ctx) {
        this.ctx = ctx;
        mensagensNotificadas = new ArrayList<>();
        usuariosRemetentes = new ArrayList<>();
        LocalBroadcastManager.getInstance(ctx).registerReceiver(mReceiver, new IntentFilter(IntentType.NOTIFICAR_MENSAGEM.toString()));
    }
}
