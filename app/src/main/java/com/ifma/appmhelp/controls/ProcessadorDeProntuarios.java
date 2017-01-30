package com.ifma.appmhelp.controls;

import android.content.Context;
import android.widget.Toast;

import com.ifma.appmhelp.daos.PacienteDao;
import com.ifma.appmhelp.daos.ProntuarioCidDao;
import com.ifma.appmhelp.daos.ProntuarioDao;
import com.ifma.appmhelp.daos.ProntuarioMedicamentoDao;
import com.ifma.appmhelp.models.Cid;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.ProntuarioCid;
import com.ifma.appmhelp.models.ProntuarioMedicamento;
import com.ifma.appmhelp.models.ProntuarioParaEnvio;
import com.ifma.appmhelp.models.UsuarioLogado;

/**
 * Created by leo on 1/11/17.
 */

public class ProcessadorDeProntuarios implements ProcessadorDeMensagens {

    @Override
    public void processar(Context ctx, Mensagem mensagem) throws Exception {

        ProntuarioParaEnvio prontuarioParaEnvio = ProntuarioParaEnvio.fromJson(mensagem.getMsg());
        Paciente paciente = (Paciente) UsuarioLogado.getInstance().getModelo();

        if (paciente.getProntuario() == null)
            prontuarioParaEnvio.getProntuario().setId(null);
        else
            prontuarioParaEnvio.getProntuario().setId(paciente.getProntuario().getId());

        new ProntuarioDao(ctx).persistir(prontuarioParaEnvio.getProntuario(), true);
        paciente.setProntuario(prontuarioParaEnvio.getProntuario());
        new PacienteDao(ctx).persistir(paciente, false);

        //Adiciono cids
        ProntuarioCidDao prontuarioCidDao = new ProntuarioCidDao(ctx);
        prontuarioCidDao.removerTodos(paciente.getProntuario());
        for (Cid cid : prontuarioParaEnvio.getCids()){
            prontuarioCidDao.persistir(new ProntuarioCid(paciente.getProntuario(), cid), false);
        }

        //Adiciono medicamentos
        ProntuarioMedicamentoDao prontuarioMedicamentoDao = new ProntuarioMedicamentoDao(ctx);
        prontuarioMedicamentoDao.removerTodos(paciente.getProntuario());

        for (ProntuarioParaEnvio.MedicamentoParaEnvio medicamento : prontuarioParaEnvio.getMedicamentos()){
            prontuarioMedicamentoDao.persistir(new ProntuarioMedicamento(paciente.getProntuario(),
                                               medicamento.getMedicamento(), medicamento.getDoses()), false);
        }

        Toast.makeText(ctx, "Prontuário atualizado", Toast.LENGTH_SHORT).show();
    }

}
