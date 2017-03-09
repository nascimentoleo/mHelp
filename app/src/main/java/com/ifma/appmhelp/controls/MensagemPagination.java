package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.daos.MensagemDao;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Ocorrencia;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 2/27/17.
 */

public class MensagemPagination extends Pagination<Mensagem> {

    private Ocorrencia ocorrencia;
    private MensagemDao dao;

    public MensagemPagination(Context ctx, Ocorrencia ocorrencia) {
        this.ocorrencia = ocorrencia;
        dao = new MensagemDao(ctx);
    }

    @Override
    public List<Mensagem> getRegistros(int offset) throws SQLException {
        List<Mensagem> mensagens = dao.
                getMensagensByOcorrencia(Long.valueOf(offset), Long.valueOf(offset + qtdDeRegistros), ocorrencia);
        return  mensagens;
    }
}
