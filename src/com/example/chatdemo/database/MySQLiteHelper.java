package com.example.chatdemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jeffreyfried on 4/24/15.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "chatdemo.db";
    private static final int DATABASE_VERSION = 1;
    public static final String COL_ID = "_id";

    public static final String TABLE_MESSAGES = "messages";
    public static final String COL_MSG = "msg";
    public static final String COL_FROM = "from";
    public static final String COL_TO = "to";
    public static final String COL_AT = "at";

    public static final String TABLE_PROFILES = "profiles";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_COUNT = "count";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_MESSAGES
                + " (_id integer primary key autoincrement, msg text, email text, email2 text, at datetime default current_timestamp);");
        db.execSQL("create table " + TABLE_PROFILES
                + " (_id integer primary key autoincrement, name text, email text unique, count integer default 0);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("drop table if exists " + TABLE_MESSAGES);
        db.execSQL("drop table if exists " + TABLE_PROFILES);
        this.onCreate(db);
    }
}
