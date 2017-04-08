package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.daos.MensagemDao;
import com.ifma.appmhelp.daos.OcorrenciaDao;
import com.ifma.appmhelp.daos.UsuarioDao;
import com.ifma.appmhelp.lib.BlowfishCrypt;
import com.ifma.appmhelp.lib.JidTranslator;
import com.ifma.appmhelp.models.ConexaoXMPP;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Usuario;

import org.jivesoftware.smack.packet.Message;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by leo on 12/20/16.
 */

public class MensagemController extends BaseController{

    public MensagemController(Context ctx) {
        super(ctx);
    }

    public void enviaMensagem(Usuario usuario, Mensagem mensagem) throws Exception {

        mensagem.preparaParaEnvio();

        String jId = JidTranslator.getjId(ConexaoXMPP.getInstance().getConexao(),usuario.getLogin());
        //Criptografa o json antes de enviar
        String jsonCriptografado = BlowfishCrypt.encrypt(mensagem.toJson());
        Message message = new Message(jId,jsonCriptografado);
        ConexaoXMPP.getInstance().getConexao().sendStanza(message);
    }

    public void salvarMensagem(Mensagem mensagem) throws SQLException {

        if (mensagem.getData() == null)
            mensagem.setData(new Date());

        if (mensagem.getUsuario() != null)
            if (mensagem.getUsuario().getId() == null)
                new UsuarioDao(ctx).carregaId(mensagem.getUsuario());

        if (mensagem.getOcorrencia() != null)
            if (mensagem.getOcorrencia().getId() == null)
                new OcorrenciaDao(ctx).carregaId(mensagem.getOcorrencia());

        new MensagemDao(ctx).persistir(mensagem, true);
    }

}
