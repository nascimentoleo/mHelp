package com.ifma.appmhelp;

import android.content.Context;

import com.ifma.appmhelp.daos.PacientesDao;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.enums.Sexo;
import com.ifma.appmhelp.enums.TipoSanguineo;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Prontuario;
import com.ifma.appmhelp.models.Usuario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created by leo on 12/23/16.
 */

@RunWith(RobolectricTestRunner.class)
public class ProntuarioTeste  {

    private Paciente paciente;
    private DbSqlHelper dbHelper;
    private Context ctx;

    @Before
    public void initialize(){
        ctx = RuntimeEnvironment.application;
        DbSqlHelper.getHelper(ctx);

        this.paciente = new Paciente(new Usuario("teste",""));
        this.paciente.setProntuario(new Prontuario());
        this.paciente.getProntuario().setSexo(Sexo.MASCULINO);
        this.paciente.getProntuario().setTipoSanguineo(TipoSanguineo.A_NEGATIVO);
        this.paciente.getProntuario().setNomeDaMae("Mae");
        this.paciente.getProntuario().setNomeDaMae("Pai");
        this.paciente.getProntuario().setIdade(12);
    }


    @Test
    public void tentaCadastrarProntuario()  {
        try {

            PacientesDao dao = new PacientesDao(ctx);
            dao.persistir(this.paciente, true);
            Paciente pacienteDB = dao.getPacienteByUsuario(paciente.getUsuario());
            assertEquals(paciente, pacienteDB);
        } catch (SQLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

}
