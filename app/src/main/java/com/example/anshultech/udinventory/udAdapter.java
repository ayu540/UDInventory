package com.example.anshultech.udinventory;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anshultech.udinventory.Data.udConract;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by AnshulTech on 13-Feb-18.
 */

public class udAdapter extends CursorAdapter {

    private Cursor myCursor;

    private TextView nametext1,pricetext2,quantitytext;

    private Button salebutton,plusbutton, minusbutton;

    private static final String LOG_TAG = udAdapter.class.getName();

    public udAdapter(Context context, Cursor c) {
        super(context, c, 0);
        myCursor= c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_view,viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        TextView nametext = (TextView) view.findViewById(R.id.name);
        TextView quantitytext = (TextView) view.findViewById(R.id.quantity);
        TextView pricetext = (TextView) view.findViewById(R.id.price);
        ImageView myimage = (ImageView)view.findViewById(R.id.image);



        String lname = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        final int lquantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
        int lprice = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
        String limage = cursor.getString(cursor.getColumnIndexOrThrow("image"));

        nametext.setText(lname);
        quantitytext.setText(Integer.toString(lquantity));
        pricetext.setText(Integer.toString(lprice));


        final Context context1 = context;

        nametext1 = (TextView)view.findViewById(R.id.name);
        pricetext2 = (TextView)view.findViewById(R.id.price);
        quantitytext = (TextView)view.findViewById(R.id.quantity);


        salebutton = (Button) view.findViewById(R.id.salebutton);
        plusbutton = (Button) view.findViewById(R.id.plusubutton);
        minusbutton = (Button) view.findViewById(R.id.minusbutton);


        salebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cursor.moveToFirst()) {
                    //int id = view.getId();
                    int id = cursor.getPosition();
                   // Uri contenturi = ContentUris.withAppendedId(udConract.udConEntery.CONTENT_URI, id);

                    int quantity = lquantity - 1;

                    if (quantity < 0) {
                        Toast.makeText(context1, "Quantity reaches to zero, can not sell more", Toast.LENGTH_SHORT).show();
                        quantity = 0; //set back quantity to zero

                    }

                    ContentValues salecontent = new ContentValues();
                    salecontent.put(udConract.udConEntery.COLOUMN_QUANTITY, quantity);
                   String selection = udConract.udConEntery._ID+"=?";
                    String selectionargs = new String[]{String.valueOf(43)};

                    int a = context1.getContentResolver().update(udConract.udConEntery.CONTENT_URI, salecontent, selection, selectionargs);

                    int b = a;

                }
            }
        });
    }
}
