package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.daos.OcorrenciaDao;
import com.ifma.appmhelp.models.Ocorrencia;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 2/27/17.
 */

public class OcorrenciaPagination implements Serializable {

    private int qtdDeRegistros;

    public OcorrenciaPagination(int qtdDeRegistros) {
        this.qtdDeRegistros = qtdDeRegistros;
    }

    public List<Ocorrencia> getListaDeOcorrencias(Context ctx, int offset) throws SQLException {
        List<Ocorrencia> ocorrencias = new OcorrenciaDao(ctx).
                getOcorrencias(Long.valueOf(offset), Long.valueOf(offset + qtdDeRegistros));

        return  ocorrencias;
    }
}
