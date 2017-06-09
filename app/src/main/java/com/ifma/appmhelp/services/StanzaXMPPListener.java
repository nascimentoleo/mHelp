package com.ifma.appmhelp.services;

import android.content.Context;
import android.widget.Toast;

import com.ifma.appmhelp.lib.BlowfishCrypt;
import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.processors.ProcessadorDeStanzas;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

/**
 * Created by leo on 12/20/16.
 */

public class StanzaXMPPListener implements StanzaListener{

    private Context ctx;

    public StanzaXMPPListener(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
        String loginConectado = ConexaoXMPP.getInstance().getConexao().getUser();
        //Verifica se a stanza foi enviada para outro usuário, e não para o próprio logado
        if(packet.getFrom() != null && packet.getFrom() != loginConectado){
            if (packet.getClass() == Message.class) {
                try {
                    this.processarMensagem((Message) packet);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ctx, "Erro ao processar mensagem: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void processarMensagem(Message message) throws Exception {
        //Extrai o json e descriptografa
        String jsonCriptografado = message.getBody().toString();
        Mensagem mensagem = Mensagem.fromJson(BlowfishCrypt.decrypt(jsonCriptografado));
        //Encaminha a mensagem para ser processada, de acordo com seu tipo
        ProcessadorDeStanzas processador = mensagem.getTipo().getProcessador();
        processador.processar(ctx,mensagem);

    }

}
