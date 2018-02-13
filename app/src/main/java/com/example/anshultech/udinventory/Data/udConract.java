package com.example.anshultech.udinventory.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by AnshulTech on 13-Feb-18.
 */

public final class udConract {
    public static final String CONTENT_AUTHORITY= "com.example.anshultech.udinventory";

    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_INVENTORY= "inventory";

    //URI for multiple pets can be used in gettype()
    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

    //URI for single pets can be used in gettype()
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

    private udConract(){

    }

    public static final class udConEntery implements BaseColumns{



        public static final String TABLE_NAME = "inventory";

        public static final String _ID = BaseColumns._ID;
        public static final String COLOUMN_NAME = "name";
        public static final String COLOUMN_QUANTITY = "quantity";
        public static final String COLOUMN_PRICE = "price";
        public static final String COLOUMN_IMAGE = "image";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

    }

}
