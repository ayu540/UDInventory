package com.example.anshultech.udinventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anshultech.udinventory.Data.udConract;

public class udInventory extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor>{
    private final static String LOG_TAG=udInventory.class.getName();

    private ListView mylistview;
    private udAdapter mycursoradapter;

    //variables for device rotation
    private static final String curState = "mystate";
    private Parcelable mliststate = null;

    private TextView nametext,pricetext,quantitytext;

    private Button salebutton,plusbutton, minusbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ud_inventory);

        //set the blank adapter on creation of appp
        mylistview = (ListView) findViewById(R.id.list_view_pet);
        mycursoradapter=new udAdapter(this, null);
        mylistview.setAdapter(mycursoradapter);



        //start loading the loader
        getLoaderManager().initLoader(0,null, this);

        //shows the floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(udInventory.this, udEditorInv.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                udConract.udConEntery._ID,
                udConract.udConEntery.COLOUMN_NAME,
                udConract.udConEntery.COLOUMN_QUANTITY,
                udConract.udConEntery.COLOUMN_PRICE,
                udConract.udConEntery.COLOUMN_IMAGE
        };

        return new CursorLoader(this,udConract.udConEntery.CONTENT_URI, projection,null, null, null );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mycursoradapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mycursoradapter.swapCursor(null);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mliststate = savedInstanceState.getParcelable(curState);
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mliststate == null) {
            getLoaderManager().restartLoader(0, null, this);
        }
        if (mliststate != null) {
            mylistview.onRestoreInstanceState(mliststate);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mliststate = mylistview.onSaveInstanceState();
        outState.putParcelable(curState, mliststate);
    }

}
