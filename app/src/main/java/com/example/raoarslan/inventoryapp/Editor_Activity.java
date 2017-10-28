package com.example.raoarslan.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.raoarslan.inventoryapp.Data.DataContract;

/**
 * Created by Rao Arslan on 24-Oct-17.
 */

public class Editor_Activity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_PET_LOADER = 0;

    /** Content URI for the existing pet (null if it's a new pet) */
    private Uri mCurrentDataUri;
    private EditText mNameEditText;

    private EditText mPreiceEditText;

    private EditText mQuantity;
    private Button mIncreaseButton;
    private Button mDecreaseButton;

    /** EditText field to enter the pet's gender */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);
        Intent intent = getIntent();
        mCurrentDataUri = intent.getData();
        if (mCurrentDataUri == null) {
            setTitle("Add Data");
        } else {
            setTitle("Edit Data");
            getLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);
        }

        mNameEditText = (EditText) findViewById(R.id.edit_name);
        mPreiceEditText = (EditText) findViewById(R.id.edit_Price);
        mQuantity = (EditText) findViewById(R.id.quantity_edit);
        mIncreaseButton = (Button) findViewById(R.id.increase_quantity);
        mDecreaseButton= (Button) findViewById(R.id.decrease_quantity);

        mIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumOneToQuantity();

            }

        });
        mDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtractOneToQuantity();
            }
        });

    }
    private void savePet() {
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPreiceEditText.getText().toString().trim();
        Integer quantity =Integer.parseInt (mQuantity.getText().toString());
        if (mCurrentDataUri == null && TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) ) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DataContract.DataEntry.COLUMN_DATA_NAME, nameString);
        values.put(DataContract.DataEntry.COLUMN_DATA_PRICE, priceString);
        values.put(String.valueOf(DataContract.DataEntry.COLUMN_QUANTITY), quantity);


        if (mCurrentDataUri == null) {
            Uri newUri = getContentResolver().insert(DataContract.DataEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, "Couldnt Save",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Saved Succesfully",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentDataUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, "Couldnt Save",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, "Saved Successfully",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void subtractOneToQuantity() {
        String previousValueString = mQuantity.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            return;
        } else if (previousValueString.equals("0")) {
            return;
        } else {
            previousValue = Integer.parseInt(previousValueString);
            mQuantity.setText(String.valueOf(previousValue - 1));
        }
    }

    private void sumOneToQuantity() {
        String previousValueString = mQuantity.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            previousValue = 0;
        } else {
            previousValue = Integer.parseInt(previousValueString);
        }
        mQuantity.setText(String.valueOf(previousValue + 1));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                savePet();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.order:
                insertData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void insertData() {
        ContentValues values = new ContentValues();
        values.put(DataContract.DataEntry.COLUMN_DATA_NAME, "Test Data");
        values.put(DataContract.DataEntry.COLUMN_DATA_PRICE, "100$");
        values.put(String.valueOf(DataContract.DataEntry.COLUMN_ORDER_QUANTITY), 10);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this Data");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteData();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteData() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentDataUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentDataUri, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, "Failed",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, "Data DELETED",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DataContract.DataEntry._ID,
                DataContract.DataEntry.COLUMN_DATA_NAME,
                DataContract.DataEntry.COLUMN_DATA_PRICE,
                String.valueOf(DataContract.DataEntry.COLUMN_QUANTITY)
                };
        String[] projection_order={

                DataContract.DataEntry.ORDER_ID,
                DataContract.DataEntry.COLUMN_ORDER_NAME,
                DataContract.DataEntry.COLUMN_ORDER_PRICE,
                DataContract.DataEntry.COLUMN_ORDER_QUANTITY
        };
        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,
                mCurrentDataUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(DataContract.DataEntry.COLUMN_DATA_NAME);
            int breedColumnIndex = cursor.getColumnIndex(DataContract.DataEntry.COLUMN_DATA_PRICE);
            int genderColumnIndex = cursor.getColumnIndex(String.valueOf(DataContract.DataEntry.COLUMN_QUANTITY));


            String name = cursor.getString(nameColumnIndex);
            String price = cursor.getString(breedColumnIndex);
            int quantity = cursor.getInt(genderColumnIndex);


            mNameEditText.setText(name);
            mPreiceEditText.setText(price);
            mQuantity.setText(Integer.toString(quantity));

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mPreiceEditText.setText("");
        mQuantity.setText("");

    }
}

