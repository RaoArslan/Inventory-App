package com.example.raoarslan.inventoryapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rao Arslan on 24-Oct-17.
 */

public class DataDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "data.db";

    private static final int DATABASE_VERSION = 1;

    public DataDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_DATA_TABLE =  "CREATE TABLE " + DataContract.DataEntry.TABLE_NAME + " ("
                + DataContract.DataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DataContract.DataEntry.COLUMN_DATA_NAME + " TEXT NOT NULL, "
                + DataContract.DataEntry.COLUMN_DATA_PRICE + " TEXT NOT NULL,"
                + DataContract.DataEntry.COLUMN_QUANTITY + " INTEGER NOT NULL );"
                ;
        String SQL_CRAETE_ORDER_TABLE= "CREATE TABLE ";
        db.execSQL(SQL_CREATE_DATA_TABLE);

    }
    public void sellOneItem(long itemId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        int newQuantity = 0;
        if (quantity > 0) {
            newQuantity = quantity -1;
        }
        ContentValues values = new ContentValues();
        values.put(DataContract.DataEntry.COLUMN_QUANTITY, newQuantity);
        String selection = DataContract.DataEntry._ID + "=?";
        String[] selectionArgs = new String[] { String.valueOf(itemId) };
        db.update(DataContract.DataEntry.TABLE_NAME,
                values, selection, selectionArgs);
    }
    public Cursor readStock() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                DataContract.DataEntry._ID,
                DataContract.DataEntry.COLUMN_DATA_NAME,
                DataContract.DataEntry.COLUMN_DATA_PRICE,
                DataContract.DataEntry.COLUMN_QUANTITY,

        };
        Cursor cursor = db.query(
                DataContract.DataEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
