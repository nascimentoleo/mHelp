package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.lib.BlowfishCrypt;
import com.ifma.appmhelp.lib.JidTranslator;
import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Usuario;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by leo on 12/20/16.
 */

public class MensagemController extends BaseController{

    public MensagemController(Context ctx) {
        super(ctx);
    }

    public void enviaMensagem(Usuario usuario, Mensagem mensagem) throws Exception {
        String jId = JidTranslator.getjId(ConexaoXMPP.getInstance().getConexao(),usuario.getLogin());
        //Criptografa o json antes de enviar
        String jsonCriptografado = BlowfishCrypt.encrypt(mensagem.toJson());
        Message message = new Message(jId,jsonCriptografado);
        ConexaoXMPP.getInstance().getConexao().sendStanza(message);

    }
}
