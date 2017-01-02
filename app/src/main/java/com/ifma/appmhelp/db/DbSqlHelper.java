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
public class DbSqlHelper extends OrmLiteSqliteOpenHelper {

   private String msgErro;
   private Context ctx;

   private static DbSqlHelper instance = null;

    public synchronized static DbSqlHelper getHelper(Context context) {
        if(instance==null)
            instance = new DbSqlHelper(context);
        return instance;
    }

   private DbSqlHelper(Context context) {
       super(context, DbInfo.getNomeBanco(), null, DbInfo.getVersaoBanco(), R.raw.ormlite_config);
       this.ctx = context;
       checkDatabaseVersion();
   }

   public String getMsgErro() {
        return this.msgErro;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            for(Class classeDb : DbClass.getClasses())
                TableUtils.createTableIfNotExists(connectionSource, classeDb);

            //Inserindo cids
            DbPopulation.insertCid(ctx, this);
            //Inserindo medicamentos
            DbPopulation.insertMedicamentos(ctx, this);

           } catch (SQLException e) {
            this.msgErro = e.getMessage();
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            for(Class classeDb : DbClass.getClasses())
                TableUtils.dropTable(connectionSource, classeDb, true);

            this.onCreate(database, connectionSource);
        } catch (SQLException e) {
            this.msgErro = e.getMessage();
            e.printStackTrace();
        }
    }

    private void checkDatabaseVersion() {
        SQLiteDatabase db = this.getWritableDatabase();
    }


}
