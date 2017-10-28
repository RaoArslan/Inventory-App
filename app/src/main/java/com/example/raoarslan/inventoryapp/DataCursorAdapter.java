package com.example.raoarslan.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.raoarslan.inventoryapp.Data.DataContract;

import static com.example.raoarslan.inventoryapp.R.id.quantity;


/**
 * Created by Rao Arslan on 25-Oct-17.
 */

public class DataCursorAdapter extends CursorAdapter {
    public DataCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }
 MainActivity activity;
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }
    private Uri mCurrentDataUri;
    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        TextView textView1= (TextView)view.findViewById(R.id.name);
        TextView textView2= (TextView)view.findViewById(R.id.price);
        final TextView textView3= (TextView)view.findViewById(quantity);

        int nameColumnIndex= cursor.getColumnIndex(DataContract.DataEntry.COLUMN_DATA_NAME);
        int priceColumnIndex = cursor.getColumnIndex(DataContract.DataEntry.COLUMN_DATA_PRICE);
        int quantityColumnIndex= cursor.getColumnIndex(DataContract.DataEntry.COLUMN_QUANTITY);
        //quantityColumnIndex = cursor.getInt(cursor.getColumnIndex(DataContract.DataEntry.COLUMN_QUANTITY));
        final long id = cursor.getLong(cursor.getColumnIndex(DataContract.DataEntry._ID));
        String Name = cursor.getString(nameColumnIndex);
        String Price = cursor.getString(priceColumnIndex);
        String quantity= cursor.getString(quantityColumnIndex);
        textView1.setText(Name);
        textView2.setText(Price);
        textView3.setText(quantity);



    }


}
