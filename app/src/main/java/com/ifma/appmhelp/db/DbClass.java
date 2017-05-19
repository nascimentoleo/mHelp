package com.ifma.appmhelp.db;

import com.ifma.appmhelp.models.Anexo;
import com.ifma.appmhelp.models.Cid;
import com.ifma.appmhelp.models.Medicamento;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.MedicoPaciente;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Ocorrencia;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Prontuario;
import com.ifma.appmhelp.models.ProntuarioCid;
import com.ifma.appmhelp.models.ProntuarioMedicamento;
import com.ifma.appmhelp.models.Usuario;

/**
 * Created by leo on 11/29/16.
 */
public class DbClass {

    //Modelos referentes à tabelas no banco
    private static final Class<?>[] classes = new Class[] {
            Usuario.class,
            Medico.class,
            Paciente.class,
            MedicoPaciente.class,
            Prontuario.class,
            ProntuarioCid.class,
            Medicamento.class,
            Cid.class,
            ProntuarioMedicamento.class,
            Ocorrencia.class,
            Mensagem.class,
            Anexo.class};


    public static Class<?>[] getClasses() {
        return classes;
    }
}
