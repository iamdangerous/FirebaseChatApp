package com.android.rahul_lohra.firebasechatapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rkrde on 13-01-2017.
 */

public class KeysTable extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myDb";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "keys_table";
    public static final String COLUMN_KEY = "key";
    public KeysTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" +
                COLUMN_KEY + " TEXT PRIMARY KEY" +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public synchronized void addKeys(String key){

        if(keyAlreadyPresent(key)){
            return;
        }else {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_KEY, key);
            db.insert(TABLE_NAME,null,cv);
            db.close();

        }


    }

    public boolean keyAlreadyPresent(String key){

        boolean keyPresent = false;
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COLUMN_KEY};
        String selection = COLUMN_KEY + " = ?";
        String[] selectionArgs = { key };
        Cursor cursor = db.query(TABLE_NAME,projection,selection,selectionArgs,null,null,null);
        if(cursor.moveToFirst()){
            do{
                keyPresent = true;
            }while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return keyPresent;
    }
}
