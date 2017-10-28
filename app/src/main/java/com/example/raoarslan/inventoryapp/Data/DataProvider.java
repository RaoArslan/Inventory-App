package com.example.raoarslan.inventoryapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Rao Arslan on 24-Oct-17.
 */

public class DataProvider extends ContentProvider {
    public static final String LOG_TAG = DataContract.DataEntry.class.getSimpleName();

    private static final int DATA = 100;

    private static final int DATA_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.Data_Path, DATA);
        sUriMatcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.Data_Path + "/#", DATA_ID);
    }
    private DataDBHelper mDbHelper;
    @Override
    public boolean onCreate() {
        mDbHelper = new DataDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();


        Cursor cursor;
        cursor = database.query(DataContract.DataEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
                return insertData(uri, contentValues);


    }

    private Uri insertData(Uri uri, ContentValues values) {
        String name = values.getAsString(DataContract.DataEntry.COLUMN_DATA_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Data requires a name");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(DataContract.DataEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DATA:
                rowsDeleted = database.delete(DataContract.DataEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case DATA_ID:
                selection = DataContract.DataEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(DataContract.DataEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,

                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DATA:
                return updateData(uri, contentValues, selection, selectionArgs);
            case DATA_ID:
                selection = DataContract.DataEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateData(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateData(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(DataContract.DataEntry.COLUMN_DATA_NAME)) {
            String name = values.getAsString(DataContract.DataEntry.COLUMN_DATA_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Data requires a name");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(DataContract.DataEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DATA:
                return DataContract.DataEntry.CONTENT_LIST_TYPE;
            case DATA_ID:
                return DataContract.DataEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
