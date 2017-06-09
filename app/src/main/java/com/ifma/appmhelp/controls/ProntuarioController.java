package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.daos.PacienteDao;
import com.ifma.appmhelp.daos.ProntuarioCidDao;
import com.ifma.appmhelp.daos.ProntuarioDao;
import com.ifma.appmhelp.daos.ProntuarioMedicamentoDao;
import com.ifma.appmhelp.models.Cid;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Prontuario;
import com.ifma.appmhelp.models.ProntuarioCid;
import com.ifma.appmhelp.models.ProntuarioMedicamento;
import com.ifma.appmhelp.models.ProntuarioParaEnvio;
import com.ifma.appmhelp.models.UsuarioLogado;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 1/11/17.
 */

public class ProntuarioController extends BaseController {

    public ProntuarioController(Context ctx) {
        super(ctx);
    }

    public ProntuarioParaEnvio getProntuarioParaEnvio(Prontuario prontuario) throws SQLException, CloneNotSupportedException {

        ProntuarioParaEnvio prontuarioParaEnvio = new ProntuarioParaEnvio(UsuarioLogado.getInstance().getUsuario().clone(), prontuario.clone());

        //Limpo as informações desnecessárias do usuario antes de enviar
        prontuarioParaEnvio.limparUsuario();

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

        return prontuarioParaEnvio;

    }

    public void atualizarProntuario(ProntuarioParaEnvio prontuarioParaEnvio, Paciente paciente) throws SQLException {

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

    }
}
