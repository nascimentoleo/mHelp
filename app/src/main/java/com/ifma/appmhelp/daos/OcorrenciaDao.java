package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Ocorrencia;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by leo on 2/24/17.
 */

public class OcorrenciaDao extends BaseController implements IDao {

    public OcorrenciaDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(IModel objeto, boolean updateChild) throws SQLException {
        Dao<Ocorrencia, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Ocorrencia.class);
        dao.createOrUpdate((Ocorrencia) objeto);
        return true;
    }

    @Override
    public void remover(IModel objeto, boolean updateChild) throws SQLException {
        Ocorrencia ocorrencia = (Ocorrencia) objeto;
        if (updateChild){
            for (Mensagem mensagem : ocorrencia.getMensagens())
                new MensagemDao(ctx).remover(mensagem, updateChild);
        }

        Dao<Ocorrencia,Long> dao = DbSqlHelper.getHelper(ctx).getDao(Ocorrencia.class);
        dao.delete(ocorrencia);
    }

    @Override
    public void carregaId(IModel objeto) throws SQLException {

    }
}
