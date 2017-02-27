package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.enums.TipoDeMensagem;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Ocorrencia;
import com.ifma.appmhelp.models.Usuario;

/**
 * Created by leo on 2/27/17.
 */

public class OcorrenciasController extends BaseController {

    public OcorrenciasController(Context ctx) {
        super(ctx);
    }

    public void enviarNovaOcorrencia(Usuario usuarioDestino, Ocorrencia ocorrencia) throws Exception {
        Mensagem mensagem = new Mensagem(ocorrencia.toJson(), TipoDeMensagem.NOVA_OCORRENCIA);
        MensagemController.enviaMensagem(usuarioDestino, mensagem);
    }
}
