package com.ifma.appmhelp.db;

import android.database.sqlite.SQLiteOpenHelper;

import com.clough.android.androiddbviewer.ADBVApplication;


/**
 * Created by leo on 11/29/16.
 */
public class DbManager extends ADBVApplication {

    @Override
    public SQLiteOpenHelper getDataBase() {
        return DbSqlHelper.getHelper(getApplicationContext());
    }

}