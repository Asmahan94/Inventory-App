package com.example.asmahansalem.inventoryapp.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Asmahan Salem on 9/20/2017.
 */

/**
 * Here is a Java code source for Contract class With full original notes
 * <p>
 * https://github.com/samuolis/Inventory/blob/master/app/src/main/java/com/example/android/inventory/data/StoreContract.java
 */

public class Contract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private Contract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.asmahansalem.inventoryapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_STORE = "store";

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */
    public static final class InventoryEntry implements BaseColumns {


        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STORE;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STORE;


        public final static String TABLE_NAME = "store";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_ITEM_NAME = "name";

        public final static String COLUMN_ITEM_PRICE = "price";
        public final static String COLUMN_ITEM_IMAGE = "product_image";
        public final static String COLUMN_ITEM_COUNT = "count";
        public final static String COLUMN_SUP_NAME = "supname";
        public final static String COLUMN_SUP_EMAIL = "supemail";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_STORE);

    }
}