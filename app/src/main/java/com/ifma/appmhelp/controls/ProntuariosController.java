package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.daos.ProntuarioCidDao;
import com.ifma.appmhelp.daos.ProntuarioMedicamentoDao;
import com.ifma.appmhelp.enums.TipoDeMensagem;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Prontuario;
import com.ifma.appmhelp.models.ProntuarioCid;
import com.ifma.appmhelp.models.ProntuarioMedicamento;
import com.ifma.appmhelp.models.ProntuarioParaEnvio;
import com.ifma.appmhelp.models.Usuario;

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
            ProntuarioParaEnvio prontuarioParaEnvio = new ProntuarioParaEnvio(prontuario);

            //Carrego os medicamentos
            List<ProntuarioMedicamento> medicamentos = new ProntuarioMedicamentoDao(ctx).getMedicamentos(prontuario);
            for (ProntuarioMedicamento prontuarioMedicamento : medicamentos){
                prontuarioParaEnvio.addMedicamento(prontuarioMedicamento);
            }

            //Carrego os cids
            List<ProntuarioCid> cids = new ProntuarioCidDao(ctx).getCids(prontuario);
            for (ProntuarioCid prontuarioCid : cids){
                prontuarioParaEnvio.addCid(prontuarioCid);
            }

            //Envio a mensagem
            Mensagem mensagem = new Mensagem(prontuarioParaEnvio.toJson(), TipoDeMensagem.ATUALIZACAO_PRONTUARIO);
            MensagemController.enviaMensagem(usuarioDestino, mensagem);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            this.msgErro = e.getMessage();
        }
        return false;
    }
}
