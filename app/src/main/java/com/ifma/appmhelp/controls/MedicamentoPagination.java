package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.daos.MedicamentoDao;
import com.ifma.appmhelp.models.Medicamento;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 2/27/17.
 */

public class MedicamentoPagination extends Pagination<Medicamento> {

    private MedicamentoDao dao;

    public MedicamentoPagination(Context ctx) {
        dao = new MedicamentoDao(ctx);
    }

    @Override
    public List<Medicamento> getRegistros(int offset) throws SQLException {
        return dao.getMedicamentos(Long.valueOf(offset), Long.valueOf(offset + qtdDeRegistros));
    }

    public List<Medicamento> getRegistros(int offset, String fieldname, String fieldValue) throws SQLException {
        return  dao.getMedicamentosByField(Long.valueOf(offset), Long.valueOf(offset + qtdDeRegistros), fieldname, fieldValue);
    }
}
