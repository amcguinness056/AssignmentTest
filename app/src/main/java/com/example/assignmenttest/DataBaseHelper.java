package com.example.assignmenttest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context,  String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE =
                "create table user(userId integer primary key autoincrement, userName text, " +
                "userEmail text, " + "userPassword text, " + "userCountry text);";

        try{
            db.execSQL(CREATE_USER_TABLE);
        }catch (Exception e){
            Log.e("Error", "excelption");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "user");
        onCreate(db);

    }
}
