package com.example.mirry.mywallet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mirry on 2016/9/11 9:54.
 */
public class MyOpenHelper extends SQLiteOpenHelper {

    public MyOpenHelper(Context context) {
        super(context, "myDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE accounts(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type VARCHAR(50) NOT NULL," +
                "categoryName VARCHAR(50) NOT NULL," +
                "categoryPic INTEGER NOT NULL," +
                "remark VARCHAR(500)," +
                "money INTEGER NOT NULL," +
                "color INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE wishes(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "wish VARCHAR(50) NOT NULL," +
                "wishMoney INTEGER NOT NULL," +
                "wishPic INTEGER," +
                "remark VARCHAR(500)," +
                "wishProgress INTEGER)");

        db.execSQL("CREATE TABLE statement(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type VARCHAR(50) NOT NULL," +
                "categoryPic INTEGER NOT NULL," +
                "categoryName VARCHAR(50) NOT NULL,"+
                "categoryMoney INTEGER NOT NULL," +
                "color INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
