package com.ifma.appmhelp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.models.Usuario;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 11/28/16.
 */
public class DbSqlHelper extends OrmLiteSqliteOpenHelper {

    private String msgErro;

    private static final List<Class> listaDeClasses = new ArrayList<Class>(){{
        add(Usuario.class);
    }};

    public DbSqlHelper(Context context) {
        super(context, DbInfo.getNomeBanco(), null, DbInfo.getVersaoBanco(), R.raw.ormlite_config);
        checkDatabaseVersion();
    }


    public String getMsgErro() {
        return this.msgErro;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            for(Class classeDb : this.listaDeClasses)
                TableUtils.createTable(connectionSource, classeDb);
        } catch (SQLException e) {
            this.msgErro = e.getMessage();
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            for(Class classeDb : this.listaDeClasses)
                TableUtils.dropTable(connectionSource, classeDb, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            this.msgErro = e.getMessage();
            e.printStackTrace();
        }
    }

    public void checkDatabaseVersion() {
        SQLiteDatabase db = this.getWritableDatabase();
    }


}
