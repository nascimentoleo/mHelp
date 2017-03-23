package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.daos.OcorrenciaDao;
import com.ifma.appmhelp.models.Ocorrencia;

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
        return dao.getOcorrencias(Long.valueOf(offset), Long.valueOf(offset + qtdDeRegistros));
    }
}
