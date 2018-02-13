package com.example.anshultech.udinventory.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by AnshulTech on 13-Feb-18.
 */



public class udProvider extends ContentProvider {

    private static final String LOG_TAG= udProvider.class.getName();

    private udDbHelper L_varinvdbhelper;

    private static final int FULL_DATA = 100;
    private static final int SINGLE_ROW_DATA = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(udConract.CONTENT_AUTHORITY, udConract.PATH_INVENTORY, FULL_DATA);
        sUriMatcher.addURI(udConract.CONTENT_AUTHORITY,udConract.PATH_INVENTORY+"/#",SINGLE_ROW_DATA);

    }

    @Override
    public boolean onCreate() {
        L_varinvdbhelper= new udDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionargs, String sortorder) {
        SQLiteDatabase database = L_varinvdbhelper.getReadableDatabase();
        Cursor cursor;
        int match  = sUriMatcher.match(uri);
        switch (match){

            case FULL_DATA:{
                cursor = database.query(udConract.udConEntery.TABLE_NAME,projection,selection,selectionargs,null,null,sortorder);
                break;
            }
            case SINGLE_ROW_DATA:{
                selection = udConract.udConEntery._ID+"=?";
                selectionargs= new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor =database.query(udConract.udConEntery.TABLE_NAME,projection,selection,selectionargs,null, null,sortorder);
                break;
            }
            default:{
                throw new IllegalArgumentException("Connot process uri: "+uri);
            }
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FULL_DATA:
                return udConract.CONTENT_LIST_TYPE;
            case SINGLE_ROW_DATA:
                return udConract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case FULL_DATA: {
                return insertPet(uri, contentValues);
            }
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertPet(Uri uri, ContentValues values) {

        String name= values.getAsString(udConract.udConEntery.COLOUMN_NAME);
        if(name ==  null){
            throw new IllegalArgumentException("name requires");
        }
        Integer quantity = values.getAsInteger(udConract.udConEntery.COLOUMN_QUANTITY);
        if(quantity ==null||(quantity !=null&&quantity <0)){
            throw new IllegalArgumentException("quantity can not be null");
        }
        Integer price = values.getAsInteger(udConract.udConEntery.COLOUMN_PRICE);
        if((price !=  null && price < 0)||price == null){
            throw new IllegalArgumentException("price can not be negative or null");
        }

        SQLiteDatabase database = L_varinvdbhelper.getWritableDatabase();
        long id = database.insert(udConract.udConEntery.TABLE_NAME, null,values);

        if (id== -1){
            Log.e(LOG_TAG,"No data has been iseerted"+uri);

        }

        getContext().getContentResolver().notifyChange(uri,null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionargs) {
        SQLiteDatabase database = L_varinvdbhelper.getWritableDatabase();

        int rowsdeleted;

        int match  = sUriMatcher.match(uri);
        switch (match){

            case FULL_DATA:{

                rowsdeleted=   database.delete(udConract.udConEntery.TABLE_NAME,selection,selectionargs);
                break;
            }
            case SINGLE_ROW_DATA:{
                selection = udConract.udConEntery._ID+"=?";
                selectionargs= new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsdeleted=  database.delete(udConract.udConEntery.TABLE_NAME,selection,selectionargs);
                break;
            }
            default:{
                throw new IllegalArgumentException("Deletion is not supported, try again: "+uri);
            }
        }
        if(rowsdeleted!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsdeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionargs) {
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case FULL_DATA:{
                return updatePet(uri, contentValues, selection,selectionargs);
            }
            case SINGLE_ROW_DATA:{
                selection = udConract.udConEntery._ID+"=?";
                selectionargs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection,selectionargs);
            }
            default:{
                throw new IllegalArgumentException("Update is no completed"+uri);
            }
        }

    }

    public int updatePet(Uri uri, ContentValues values, String selection, String[] selectionargs) {

        if(values.containsKey(udConract.udConEntery.COLOUMN_NAME)) {
            String name = values.getAsString(udConract.udConEntery.COLOUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("name requires for update");
            }
        }
        if(values.containsKey(udConract.udConEntery.COLOUMN_QUANTITY)) {
            Integer quantity = values.getAsInteger(udConract.udConEntery.COLOUMN_QUANTITY);
            if(quantity ==null||(quantity !=null&&quantity <0)){
                throw new IllegalArgumentException("quantity can not blank or less than zero");
            }
        }

        if(values.containsKey(udConract.udConEntery.COLOUMN_PRICE)) {
            Integer price = values.getAsInteger(udConract.udConEntery.COLOUMN_PRICE);
            if((price !=  null && price < 0)||price == null){
                throw new IllegalArgumentException("price can not be negative or null");
            }
        }

        if(values.size()==0){
            return 0;
        }

        SQLiteDatabase database = L_varinvdbhelper.getWritableDatabase();

        int rowupdat =database.update(udConract.udConEntery.TABLE_NAME,values,selection,selectionargs);

        if (rowupdat!=0 ){

            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowupdat;
    }

}
