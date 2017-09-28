package com.example.asmahansalem.inventoryapp;

import static com.example.asmahansalem.inventoryapp.Data.Contract.InventoryEntry;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Here is a Java code source for class Detail With full original notes
 * <p>
 * https://github.com/samuolis/Inventory/blob/master/app/src/main/java/com/example/android/inventory/DetailsActivity.java
 */

public class Detail extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = Detail.class.getSimpleName();

    final Context mContext = this;

    /**
     * Identifier for the item data loader
     */
    private static final int EXISTING_ITEM_LOADER = 0;

    /**
     * Content URI for the existing item (null if it's a new item)
     */
    private Uri mCurrentItemUri;

    /**
     * EditText field to enter the item's name
     */
    private TextView mNameEditText;

    private TextView mPriceEditText;

    private TextView mCountEditText;

    private TextView mSupNameEditText;

    private TextView mSupEmailEditText;
    private String supEmail;
    private String supName;
    private String name;

    private ImageView plus;
    private ImageView minus;
    private String countNumberString;
    private int countNumber;
    private ImageView mImageProduct;

    /**
     * Boolean flag that keeps track of whether the item has been edited (true) or not (false)
     */
    private boolean mItemHasChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mNameEditText = (TextView) findViewById(R.id.editProduct);
        mPriceEditText = (TextView) findViewById(R.id.price_edit);
        mCountEditText = (TextView) findViewById(R.id.quantity_edit);
        mSupNameEditText = (TextView) findViewById(R.id.edit_supplier_name);
        mSupEmailEditText = (TextView) findViewById(R.id.edit_email);
        plus = (ImageView) findViewById(R.id.increase_quantity);
        minus = (ImageView) findViewById(R.id.decrease_quantity);
        mImageProduct = (ImageView) findViewById(R.id.image_view);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus(v);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minus(v);
            }
        });

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new item or editing an existing one.
        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);

        plus.setOnTouchListener(mTouchListener);
        minus.setOnTouchListener(mTouchListener);
    }

    public void sendSupplier(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", supEmail, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, name);
        intent.putExtra(Intent.EXTRA_TEXT, "Dear " + supName + ",");
        startActivity(intent);
    }

    public void plus(View view) {
        countNumberString = mCountEditText.getText().toString().trim();
        countNumber = Integer.parseInt(countNumberString);
        countNumber = countNumber + 1;
        mCountEditText.setText(String.valueOf(countNumber));
    }

    public void minus(View view) {
        countNumberString = mCountEditText.getText().toString().trim();
        countNumber = Integer.parseInt(countNumberString);
        if (countNumber >= 1)
            countNumber = countNumber - 1;
        else countNumber = 0;
        mCountEditText.setText(String.valueOf(countNumber));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new item, hide the "Delete" menu item.

        MenuItem menuItem = menu.findItem(R.id.action_save);
        menuItem.setVisible(false);

        return true;
    }

    @Override
    public void onBackPressed() {
        // If the item hasn't changed, continue with handling back button press
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }
        String countString = mCountEditText.getText().toString().trim();
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_ITEM_COUNT, countString);
        int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_edit:
                Intent intent = new Intent(Detail.this, AddProduct.class);

                // Form the content URI that represents the specific Item that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link ItemEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.Items/Items/2"
                // if the Item with ID 2 was clicked on.

                if (mItemHasChanged) {
                    String countString = mCountEditText.getText().toString().trim();
                    ContentValues values = new ContentValues();
                    values.put(InventoryEntry.COLUMN_ITEM_COUNT, countString);
                    int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);
                }
                intent.setData(mCurrentItemUri);

                // Launch the {@link EditorActivity} to display the data for the current Item.
                startActivity(intent);

                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete_item:
                showDeleteConfirmationDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Delete this item ?");
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the item.
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the item.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the item in the database.
     */
    private void deleteItem() {
        // Only perform the delete if this is an existing item.
        if (mCurrentItemUri != null) {
            // Call the ContentResolver to delete the item at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentitemUri
            // content URI already identifies the item that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, ("error delleting item"),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, ("Deleted succesfully"),
                        Toast.LENGTH_SHORT).show();
            }
        }
        // Close the activity
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all item attributes, define a projection that contains
        // all columns from the item table
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_ITEM_NAME,
                InventoryEntry.COLUMN_ITEM_PRICE,
                InventoryEntry.COLUMN_ITEM_COUNT,
                InventoryEntry.COLUMN_SUP_NAME,
                InventoryEntry.COLUMN_SUP_EMAIL,
                InventoryEntry.COLUMN_ITEM_IMAGE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentItemUri,         // Query the content URI for the current item
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of item attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_ITEM_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_ITEM_PRICE);
            int countColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_ITEM_COUNT);
            int supNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUP_NAME);
            int supEmailColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUP_EMAIL);
            int imageColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_ITEM_IMAGE);

            // Extract out the value from the Cursor for the given column index
            name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int count = cursor.getInt(countColumnIndex);
            supName = cursor.getString(supNameColumnIndex);
            supEmail = cursor.getString(supEmailColumnIndex);
            byte[] image = cursor.getBlob(imageColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mPriceEditText.setText(Integer.toString(price));
            mCountEditText.setText(Integer.toString(count));
            mSupNameEditText.setText(supName);
            mSupEmailEditText.setText(supEmail);

            Bitmap itemBitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            mImageProduct.setImageBitmap(itemBitmap);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mCountEditText.setText("");
        mSupNameEditText.setText("");
        mSupEmailEditText.setText("");

    }
}
