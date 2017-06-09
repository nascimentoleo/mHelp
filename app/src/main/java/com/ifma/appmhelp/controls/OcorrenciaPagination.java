package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.daos.OcorrenciaDao;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Ocorrencia;
import com.ifma.appmhelp.models.UsuarioLogado;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 2/27/17.
 */

public class OcorrenciaPagination extends Pagination<Ocorrencia> {

    private OcorrenciaDao dao;

    public OcorrenciaPagination(Context ctx) {
        dao = new OcorrenciaDao(ctx);
    }

    @Override
    public List<Ocorrencia> getRegistros(int offset) throws SQLException {

        if (UsuarioLogado.getInstance().getModelo().getClass() == Medico.class)
            return dao.getOcorrenciasByField(Long.valueOf(offset), Long.valueOf(offset + qtdDeRegistros),
                    "medico_id", UsuarioLogado.getInstance().getModelo().getId());

        return dao.getOcorrenciasByField(Long.valueOf(offset), Long.valueOf(offset + qtdDeRegistros),
                "paciente_id", UsuarioLogado.getInstance().getModelo().getId());

    }
}
