package com.example.assignmenttest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LoginDataBaseAdapter {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "login.db";
    private static SQLiteDatabase db;
    private static DataBaseHelper dbHelper;


    public LoginDataBaseAdapter(Context context){
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public LoginDataBaseAdapter open() throws SQLException{
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance(){
        return db;
    }

    public String getSingleEntry(String userName){
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("user", null, "userName=?",
                new String[]{userName}, null, null,null);
        if(cursor.getCount()<1)
            return "NOT EXIST";
        cursor.moveToFirst();
        String getPassword = cursor.getString(cursor.getColumnIndex("userPassword"));
        return getPassword;
    }

    public void insertEntry(String userName, String userEmail, String userPassword, String userCountry){
        ContentValues newValues = new ContentValues();
        newValues.put("userName", userName);
        newValues.put("userEmail", userEmail);
        newValues.put("userPassword", userPassword);
        newValues.put("userCountry", userCountry);

        db.insert("user", null, newValues);
    }
}
