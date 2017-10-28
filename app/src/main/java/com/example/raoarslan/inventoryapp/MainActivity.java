package com.example.raoarslan.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.raoarslan.inventoryapp.Data.DataContract;
import com.example.raoarslan.inventoryapp.Data.DataDBHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int DATA_LOADER=0;
    DataDBHelper dbHelper;
    DataCursorAdapter adapter;
 DataCursorAdapter mCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, Editor_Activity.class);
                startActivity(intent);

            }
        });
        ListView listView= (ListView)findViewById(R.id.list);
        mCursorAdapter = new DataCursorAdapter(this, null);
        listView.setAdapter(mCursorAdapter);
        getLoaderManager().initLoader(DATA_LOADER,null, this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, Editor_Activity.class);
                Uri currentPetUri= ContentUris.withAppendedId(DataContract.DataEntry.CONTENT_URI,id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });

     }
    public void clickOnSale(long id, int quantity) {
        dbHelper.sellOneItem(id, quantity);
        adapter.swapCursor(dbHelper.readStock());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertData();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllData() {
        int rowsDeleted = getContentResolver().delete(DataContract.DataEntry.CONTENT_URI, null, null);

    }

    private void insertData() {
        ContentValues values = new ContentValues();
        values.put(DataContract.DataEntry.COLUMN_ORDER_NAME, "Test Data");
        values.put(DataContract.DataEntry.COLUMN_ORDER_PRICE, "100$");
        values.put(String.valueOf(DataContract.DataEntry.COLUMN_QUANTITY), 10);
        Uri newUri = getContentResolver().insert(DataContract.DataEntry.CONTENT_URI, values);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DataContract.DataEntry._ID,
                DataContract.DataEntry.COLUMN_DATA_NAME,
                DataContract.DataEntry.COLUMN_DATA_PRICE,
                String.valueOf(DataContract.DataEntry.COLUMN_QUANTITY)
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,
                DataContract.DataEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void clickOnViewItem(long id) {
        Intent intent = new Intent(this, Editor_Activity.class);
        intent.putExtra("itemId", id);
        startActivity(intent);
    }
}
