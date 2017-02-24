package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.IModel;
import com.ifma.appmhelp.models.Mensagem;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by leo on 2/24/17.
 */

public class MensagemDao extends BaseController implements IDao {

    public MensagemDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(IModel objeto, boolean updateChild) throws SQLException {
        Dao<Mensagem, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Mensagem.class);
        dao.createOrUpdate((Mensagem) objeto);
        return true;
    }

    @Override
    public void remover(IModel objeto, boolean updateChild) throws SQLException {
        Dao<Mensagem,Long> dao = DbSqlHelper.getHelper(ctx).getDao(Mensagem.class);
        dao.delete((Mensagem) objeto);
    }

    @Override
    public void carregaId(IModel objeto) throws SQLException {

    }

}
