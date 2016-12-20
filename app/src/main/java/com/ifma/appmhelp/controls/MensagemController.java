package com.ifma.appmhelp.controls;

import com.ifma.appmhelp.lib.JidTranslator;
import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Usuario;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by leo on 12/20/16.
 */

public class MensagemController{

    public static void enviaMensagem(Usuario usuario, Mensagem mensagem) throws SmackException.NotConnectedException {
        String jId = JidTranslator.getjId(ConexaoXMPP.getInstance().getConexao(),usuario.getLogin());
        Message message = new Message(jId,mensagem.toJson());
        ConexaoXMPP.getInstance().getConexao().sendStanza(message);
    }
}
