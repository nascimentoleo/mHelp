package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.daos.CidDao;
import com.ifma.appmhelp.models.Cid;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 2/27/17.
 */

public class CidPagination extends Pagination<Cid> {

    private CidDao dao;

    public CidPagination(Context ctx) {
        dao = new CidDao(ctx);
    }

    @Override
    public List<Cid> getRegistros(int offset) throws SQLException {
        return dao.getCids(Long.valueOf(offset), Long.valueOf(offset + qtdDeRegistros));
    }

    public List<Cid> getRegistros(int offset, String fieldname, String fieldValue) throws SQLException {
        return dao.getCidsByField(Long.valueOf(offset), Long.valueOf(offset + qtdDeRegistros), fieldname, fieldValue);
    }
}
