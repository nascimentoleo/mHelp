package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.enums.TipoDeMensagem;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Prontuario;
import com.ifma.appmhelp.models.Usuario;

/**
 * Created by leo on 1/11/17.
 */

public class ProntuariosController extends BaseController {

    public ProntuariosController(Context ctx) {
        super(ctx);
    }

    public boolean enviarProntuario(Prontuario prontuario, Usuario usuarioDestino){
        Mensagem mensagem = new Mensagem(prontuario.toJson(), TipoDeMensagem.ATUALIZACAO_PRONTUARIO);
        try {
            MensagemController.enviaMensagem(usuarioDestino, mensagem);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            this.msgErro = e.getMessage();
        }
        return false;
    }
}
