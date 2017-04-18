package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Ocorrencia;
import com.ifma.appmhelp.models.Paciente;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 2/24/17.
 */

public class OcorrenciaDao extends BaseController implements IDao<Ocorrencia> {

    public OcorrenciaDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(Ocorrencia objeto, boolean updateChild) throws SQLException {
        Dao<Ocorrencia, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Ocorrencia.class);
        dao.createOrUpdate(objeto);
        return true;
    }

    @Override
    public void remover(Ocorrencia objeto, boolean updateChild) throws SQLException {
        if (updateChild){
            List<Mensagem> mensagens = new MensagemDao(ctx).getMensagensByOcorrencia(objeto);
            for (Mensagem mensagem : mensagens)
                new MensagemDao(ctx).remover(mensagem, updateChild);
        }

        Dao<Ocorrencia,Long> dao = DbSqlHelper.getHelper(ctx).getDao(Ocorrencia.class);
        dao.delete(objeto);
    }

    @Override
    public void carregaId(Ocorrencia objeto) throws SQLException {
        Dao<Ocorrencia, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Ocorrencia.class);
        objeto.setDataUltimaMensagem(null); //Ignora a data
        List<Ocorrencia> ocorrencias = dao.queryForMatching(objeto);
        if(!ocorrencias.isEmpty())
            objeto.setId(ocorrencias.get(0).getId());
    }

    public List<Ocorrencia> getOcorrencias(Long inicio, Long fim) throws SQLException {
        Dao<Ocorrencia, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Ocorrencia.class);
        QueryBuilder<Ocorrencia, Long> query = dao.queryBuilder().offset(inicio).limit(fim);
        query.orderBy("dataUltimaMensagem",false);
        PreparedQuery<Ocorrencia> prepare = query.prepare();
        return dao.query(prepare);
    }

    public boolean atualizarDataUltimaMensagem(Ocorrencia objeto) throws SQLException {
        Dao<Ocorrencia, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Ocorrencia.class);
        UpdateBuilder<Ocorrencia, Long> updateBuilder = dao.updateBuilder();
        updateBuilder.updateColumnValue("dataUltimaMensagem", objeto.getDataUltimaMensagem());
        updateBuilder.where().idEq(objeto.getId());
        updateBuilder.update();
        return true;
    }

    public void removerOcorrenciasByPaciente(Paciente paciente){

    }


}
