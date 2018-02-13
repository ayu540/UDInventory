package com.example.anshultech.udinventory.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by AnshulTech on 13-Feb-18.
 */

public class udDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "udInventory.db";


    public udDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private final static String TEXT_TYPE= " TEXT ";
    private final static String INT_TYPE= " INTEGER ";
    private final static String PRIMARY_KEY= " PRIMARY KEY ";
    private final static String AUTOINCR_TYPE= " AUTOINCREMENT ";
    private final static String NOT_NULL_TYPE= " NOT NULL ";
    private final static String DEFAULT_TYPE= " DEFAULT 0";
    private final static String COMMA_SEPERATOR= ", ";
    private final static String IMG_BLOB= " BLOB";


    private final static String CREATE_TABLE_INVENTORY= "CREATE TABLE "+ udConract.udConEntery.TABLE_NAME + "("+
            udConract.udConEntery._ID+INT_TYPE+PRIMARY_KEY+AUTOINCR_TYPE+COMMA_SEPERATOR+
            udConract.udConEntery.COLOUMN_NAME+TEXT_TYPE+NOT_NULL_TYPE+COMMA_SEPERATOR+
            udConract.udConEntery.COLOUMN_QUANTITY+INT_TYPE+NOT_NULL_TYPE+DEFAULT_TYPE+COMMA_SEPERATOR+
            udConract.udConEntery.COLOUMN_PRICE+INT_TYPE+NOT_NULL_TYPE+COMMA_SEPERATOR+
            udConract.udConEntery.COLOUMN_IMAGE+TEXT_TYPE+
            ");";
    private static final String DELETE_TABLE= "DROP TABLE IF EXISTS"+udConract.udConEntery.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_INVENTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
