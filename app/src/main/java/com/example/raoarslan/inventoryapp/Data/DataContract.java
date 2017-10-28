package com.example.raoarslan.inventoryapp.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Rao Arslan on 24-Oct-17.
 */

public class DataContract {


    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String Data_Path = "data";

    public static final class DataEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, Data_Path);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + Data_Path;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + Data_Path;

        public final static String TABLE_NAME = "data";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DATA_NAME ="_Name";
        public final static String COLUMN_DATA_PRICE ="_Price";
        public final static String COLUMN_QUANTITY = "_Quaitity";


        public final static String TABLE_ORDER_NAME= "order";
        public final static String ORDER_ID= BaseColumns._ID;
        public final static String COLUMN_ORDER_NAME ="_Name";
        public final static String COLUMN_ORDER_PRICE ="_Price";
        public final static String COLUMN_ORDER_QUANTITY = "_Quaitity";

        // public final static String COLUMN_PET_GENDER = "gender";
        //public final static String COLUMN_PET_WEIGHT = "weight";

    }
}
