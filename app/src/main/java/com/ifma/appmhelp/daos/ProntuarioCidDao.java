package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.Prontuario;
import com.ifma.appmhelp.models.ProntuarioCid;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 12/23/16.
 */

public class ProntuarioCidDao extends BaseController implements IDao<ProntuarioCid> {

    public ProntuarioCidDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(ProntuarioCid objeto, boolean updateChild) throws SQLException {
        Dao<ProntuarioCid,Long> dao = DbSqlHelper.getHelper(ctx).getDao(ProntuarioCid.class);
        dao.createOrUpdate(objeto);
        return true;
    }

    @Override
    public void remover(ProntuarioCid objeto, boolean updateChild) throws SQLException {
        Dao<ProntuarioCid, Long> dao = DbSqlHelper.getHelper(ctx).getDao(ProntuarioCid.class);
        dao.delete(objeto);
    }

    public void removerTodos(Prontuario prontuario) throws SQLException {
        Dao<ProntuarioCid, Long> dao = DbSqlHelper.getHelper(ctx).getDao(ProntuarioCid.class);
        List<ProntuarioCid> prontuarioCids = dao.queryForMatching(new ProntuarioCid(prontuario, null));
        for(ProntuarioCid prontuarioCid : prontuarioCids)
            remover(prontuarioCid, false);
    }

    public List<ProntuarioCid> getProntuariosCids(Prontuario prontuario) throws SQLException {
        Dao<ProntuarioCid,Long> dao = DbSqlHelper.getHelper(ctx).getDao(ProntuarioCid.class);
        ProntuarioCid prontuarioCid = new ProntuarioCid(prontuario, null);
        return dao.queryForMatching(prontuarioCid);
    }

    @Override
    public void carregaId(ProntuarioCid objeto) throws SQLException {
        Dao<ProntuarioCid, Long> dao = DbSqlHelper.getHelper(ctx).getDao(ProntuarioCid.class);
        List<ProntuarioCid> prontuarioCids = dao.queryForMatching(objeto);
        if(!prontuarioCids.isEmpty())
            objeto.setId(prontuarioCids.get(0).getId());

    }

    public List getCids(Prontuario prontuario) throws SQLException {
        Dao<ProntuarioCid, Long> dao = DbSqlHelper.getHelper(ctx).getDao(ProntuarioCid.class);
        return dao.queryForMatchingArgs(new ProntuarioCid(prontuario, null));
    }




}
