package com.kunyang.android.nowmission;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 坤阳 on 2017/9/9.
 */

public class SSXDBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION=1;

    private static final String DATABASE_NAME="city.db";

    public static final String TABLE_NAME = "area";

    public static final String CREATE_Tables = "create table "
            + "if not exists "
            + "province"
            + " (_id INTEGER NOT NULL PRIMARY KEY,"
            + " name varchar(200),"
            + " pid INTEGER,"
            + " levelid INTEGER"
            + ")";

    public static final String CREATE_Tables2 = "create table "
            + "if not exists "
            + "city"
            + " (_id INTEGER NOT NULL PRIMARY KEY,"
            + " name varchar(200),"
            + " pid INTEGER,"
            + " levelid INTEGER"
            + ")";
    // 建表语句-area
    public static final String CREATE_Tables3 = "create table "
            + "if not exists "
            + "country"
            + " (_id INTEGER NOT NULL PRIMARY KEY,"
            + " name varchar(200),"
            + " pid INTEGER,"
            + " levelid INTEGER"
            + ")";


    public SSXDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_Tables);
        sqLiteDatabase.execSQL(CREATE_Tables2);
        sqLiteDatabase.execSQL(CREATE_Tables3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
