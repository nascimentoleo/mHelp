package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.models.Mensagem;

/**
 * Created by leo on 1/11/17.
 */

public class ProcessadorDeProntuarios implements ProcessadorDeMensagens {

    @Override
    public void processar(Context ctx, Mensagem mensagem) throws Exception {

       /* Prontuario prontuario = Prontuario.fromJson(mensagem.getMsg());
        Paciente paciente = (Paciente) UsuarioLogado.getInstance().getModelo();

        if (paciente.getProntuario() == null)
            prontuario.setId(null);
        else
            prontuario.setId(paciente.getProntuario().getId());

        new ProntuarioDao(ctx).persistir(prontuario, true);
        paciente.setProntuario(prontuario);
        new PacienteDao(ctx).persistir(paciente, false);

        //Adiciono cids
        ProntuarioCidDao prontuarioCidDao = new ProntuarioCidDao(ctx);
        prontuarioCidDao.remover(new ProntuarioCid(prontuario, null), false);
        for (Cid cid : prontuario.getCids()){
            prontuarioCidDao.persistir(new ProntuarioCid(prontuario, cid), false);
        }
        //Adiciono medicamentos
        ProntuarioMedicamentoDao prontuarioMedicamentoDao = new ProntuarioMedicamentoDao(ctx);
        prontuarioMedicamentoDao.remover(new ProntuarioMedicamento(prontuario, null, null), false);

        Iterator iterator = prontuario.getMedicamentos().entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry registro = (Map.Entry) iterator.next();
            Medicamento medicamento = (Medicamento) registro.getKey();
            String doses            = (String) registro.getValue();
            prontuarioMedicamentoDao.persistir(new ProntuarioMedicamento(prontuario, medicamento, doses), false);
        }

        Toast.makeText(ctx, "Prontuário atualizado", Toast.LENGTH_SHORT).show();

        */
    }
}
