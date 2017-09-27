package com.example.asmahansalem.inventoryapp;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.asmahansalem.inventoryapp.Data.Contract.InventoryEntry;

import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asmahansalem.inventoryapp.Data.Contract;

/**
 * Created by Asmahan Salem on 9/20/2017.
 */

/**
 * Here is a Java code source for CursorAdapter With full original notes
 * <p>
 * https://github.com/samuolis/Inventory/blob/master/app/src/main/java/com/example/android/inventory/StoreCursorAdapter.java
 */

public class InventoryCursorAdapter extends CursorAdapter {


    private static Context mContext;
    public static final String LOG_TAG = InventoryCursorAdapter.class.getSimpleName();


    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mContext = context;
    }

    public static class ProductViewHolder {

        TextView textViewName;
        TextView textViewPrice;
        TextView textViewCount;
        ImageView buttonSale;

        // Find various views within ListView and set custom typeface on them
        public ProductViewHolder(View itemView) {

            textViewName = (TextView) itemView.findViewById(R.id.product_name);
            textViewPrice = (TextView) itemView.findViewById(R.id.price);
            textViewCount = (TextView) itemView.findViewById(R.id.quantity);
            buttonSale = (ImageView) itemView.findViewById(R.id.sale);

        }

    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list, parent, false);

        ProductViewHolder holder = new ProductViewHolder(view);

        view.setTag(holder);

        return view;
    }

    /**
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        ProductViewHolder holder = (ProductViewHolder) view.getTag();

        // Find the columns of item attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_ITEM_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_ITEM_PRICE);
        int countColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_ITEM_COUNT);

        // Read the pet attributes from the Cursor for the current item
        final int productId = cursor.getInt(cursor.getColumnIndex(Contract.InventoryEntry._ID));
        String ItemName = cursor.getString(nameColumnIndex);
        int ItemPrice = cursor.getInt(priceColumnIndex);
        final int ItemCount = cursor.getInt(countColumnIndex);

        holder.textViewName.setText(ItemName);
        holder.textViewPrice.setText(String.valueOf(ItemPrice));
        holder.textViewCount.setText(String.valueOf(ItemCount));


        holder.buttonSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri productUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, productId);
                adjustStock(context, productUri, ItemCount);
            }
        });
    }

    private void adjustStock(Context context, Uri productUri, int currentStock) {

        // Reduce stock, check if new stock is less than 0, in which case set it to 0
        int newCount;
        if (currentStock >= 1)
            newCount = currentStock - 1;
        else newCount = 0;

        // Update table with new stock of the product
        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryEntry.COLUMN_ITEM_COUNT, newCount);
        int numRowsUpdated = context.getContentResolver().update(productUri, contentValues, null, null);

    }

}