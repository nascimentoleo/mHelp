package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Prontuario;
import com.ifma.appmhelp.models.ProntuarioCid;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 12/23/16.
 */

public class ProntuarioCidDao extends BaseController implements IDao {


    public ProntuarioCidDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(IModel objeto, boolean updateChild) throws SQLException {
        Dao<ProntuarioCid,Long> dao = DbSqlHelper.getHelper(ctx).getDao(ProntuarioCid.class);
        dao.createOrUpdate((ProntuarioCid) objeto);
        return true;
    }

    @Override
    public void remover(IModel objeto, boolean updateChild) throws SQLException {
        Dao<ProntuarioCid, Long> dao = DbSqlHelper.getHelper(ctx).getDao(ProntuarioCid.class);
        dao.delete((ProntuarioCid) objeto);
    }

    public List<ProntuarioCid> getProntuariosCids(Prontuario prontuario) throws SQLException {
        Dao<ProntuarioCid,Long> dao = DbSqlHelper.getHelper(ctx).getDao(ProntuarioCid.class);
        ProntuarioCid prontuarioCid = new ProntuarioCid(prontuario, null);
        return dao.queryForMatching(prontuarioCid);
    }

    @Override
    public void carregaId(IModel objeto) throws SQLException {
        Dao<ProntuarioCid, Long> dao = DbSqlHelper.getHelper(ctx).getDao(ProntuarioCid.class);
        List<ProntuarioCid> prontuarioCids = dao.queryForMatching((ProntuarioCid) objeto);
        if(!prontuarioCids.isEmpty())
            objeto.setId(prontuarioCids.get(0).getId());

    }



}
