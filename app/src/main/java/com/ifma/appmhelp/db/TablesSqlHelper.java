package com.ifma.appmhelp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ifma.appmhelp.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by leo on 11/28/16.
 */
public class TablesSqlHelper extends OrmLiteSqliteOpenHelper {

    private String msgErro;
    private Class tableClass;

    public TablesSqlHelper(Context context, Class tableClass) {
        super(context, dbInfo.getNomeBanco(), null, dbInfo.getVersaoBanco(), R.raw.ormlite_config);
        this.tableClass = tableClass;
    }

    public String getMsgErro() {
        return this.msgErro;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, tableClass);
        } catch (SQLException e) {
            this.msgErro = e.getMessage();
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, tableClass, false);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            this.msgErro = e.getMessage();
            e.printStackTrace();
        }
    }

}
