package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.daos.ProntuarioCidDao;
import com.ifma.appmhelp.daos.ProntuarioMedicamentoDao;
import com.ifma.appmhelp.enums.TipoDeMensagem;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Prontuario;
import com.ifma.appmhelp.models.Usuario;

import java.util.HashMap;
import java.util.List;

/**
 * Created by leo on 1/11/17.
 */

public class ProntuariosController extends BaseController {

    public ProntuariosController(Context ctx) {
        super(ctx);
    }

    public boolean enviarProntuario(Prontuario prontuario, Usuario usuarioDestino){
        try {
            //Carrego os medicamentos
            HashMap medicamentos = new ProntuarioMedicamentoDao(ctx).getMedicamentos(prontuario);
            prontuario.setMedicamentos(medicamentos);

            //Carrego os cids
            List cids = new ProntuarioCidDao(ctx).getCids(prontuario);
            prontuario.setCids(cids);

            //Envio a mensagem
            Mensagem mensagem = new Mensagem(prontuario.toJson(), TipoDeMensagem.ATUALIZACAO_PRONTUARIO);
            MensagemController.enviaMensagem(usuarioDestino, mensagem);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            this.msgErro = e.getMessage();
        }
        return false;
    }
}
