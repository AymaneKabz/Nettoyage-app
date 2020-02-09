package com.example.belatarr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ProgrammingKnowledge on 4/3/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Android.db";
    public static final String TABLE_NAME = "tasks";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME_TASK";
    public static final String COL_3 = "DESCRIPTION_TASK";
    public static final String COL_4 = "USER";
    public static final String COL_5 = "DONE";
    public static final String COL_6 = "COMMENT";
    public static final String COL_7 = "DATE";
    public static final String COL_8 = "DATEFIN";
    public static final String COL_9 = "SYNCED";

    public boolean created = false;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME_TASK TEXT,DESCRIPTION_TASK TEXT,USER TEXT,DONE TEXT,COMMENT TEXT,DATE TEXT,DATEFIN TEXT,SYNCED TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }



    public boolean createData(String name_task,String description_task,String user,String done,String comment,String date,String datefin,String synced) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name_task);
        contentValues.put(COL_3,description_task);
        contentValues.put(COL_4,user);
        contentValues.put(COL_5,done);
        contentValues.put(COL_6,comment);
        contentValues.put(COL_7,date);
        contentValues.put(COL_8,datefin);
        contentValues.put(COL_9,synced);
        if(created==false)
            db.execSQL("delete from "+ TABLE_NAME);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        created=true;
        if(result == -1)
            return false;
        else
            return true;

    }




    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public boolean updateData(String name_task,String description_task,String user,String comment,String done,String datefin,String synced) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name_task);
        contentValues.put(COL_3,description_task);
        contentValues.put(COL_4,user);
        contentValues.put(COL_5,done);
        contentValues.put(COL_6,comment);
        contentValues.put(COL_8,datefin);
        contentValues.put(COL_9,synced);


        db.update(TABLE_NAME, contentValues, "NAME_TASK = ? and USER = ? ",new String[] { name_task, user});
        return true;
    }


}