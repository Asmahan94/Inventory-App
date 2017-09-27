package com.example.asmahansalem.inventoryapp.Data;

import android.content.Context;

import static com.example.asmahansalem.inventoryapp.Data.Contract.InventoryEntry;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Asmahan Salem on 9/20/2017.
 */

/**
 * Here is a Java code source for DbHelper With full original notes
 * <p>
 * https://github.com/samuolis/Inventory/blob/master/app/src/main/java/com/example/android/inventory/data/StoreDbHelper.java
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "store.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + InventoryEntry.TABLE_NAME + " ("
                + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_ITEM_PRICE + " INTEGER NOT NULL DEFAULT 0,"
                + InventoryEntry.COLUMN_ITEM_COUNT + " INTEGER NOT NULL DEFAULT 0,"
                + InventoryEntry.COLUMN_SUP_NAME + " TEXT NOT NULL,"
                + InventoryEntry.COLUMN_SUP_EMAIL + " TEXT NOT NULL,"
                + InventoryEntry.COLUMN_ITEM_IMAGE + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
