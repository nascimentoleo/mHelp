package com.ifma.appmhelp.services;

import android.content.Context;

import com.ifma.appmhelp.controls.ProcessadorDeMensagens;
import com.ifma.appmhelp.lib.BlowfishCrypt;
import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.Mensagem;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
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
        String login = ConexaoXMPP.getInstance().getConexao().getUser();
        if(packet.getFrom() != null && packet.getFrom() != login){
            if(packet.getClass() == Presence.class)
                this.processarPresenca((Presence) packet);
            else if (packet.getClass() == Message.class) {
                try {
                    this.processarMensagem((Message) packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processarMensagem(Message message) throws Exception {
        String jsonCriptografado = message.getBody().toString();
        Mensagem mensagem = Mensagem.fromJson(BlowfishCrypt.decrypt(jsonCriptografado));
        ProcessadorDeMensagens processador = mensagem.getTipo().getProcessador();
        processador.processar(ctx,mensagem);
    }

    private void processarPresenca(Presence presence){
       /* LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(ctx);
        //Recebeu uma solicitação para adicionar contato
        if(presence.getType() == Presence.Type.subscribe){
            Intent it = new Intent("solicitacao_roster");
            it.putExtra("login", JidTranslator.getLogin(presence.getFrom()));
            it.putExtra("aceitou_solicitacao", true); //Parametro utilizado para confirmar que paciente confirmou a solicitação
            lbm.sendBroadcast(it);
        //Recebeu uma solicitacao para remover contato
        }else if(presence.getType() == Presence.Type.unsubscribe){
            Intent it = new Intent("solicitacao_roster");
            it.putExtra("aceitou_solicitacao", false);
            lbm.sendBroadcast(it);
        } */

    }

}
