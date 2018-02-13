package com.example.anshultech.udinventory;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.anshultech.udinventory.Data.udConract;

/**
 * Created by AnshulTech on 13-Feb-18.
 */

public class udEditorInv extends AppCompatActivity {

    private EditText mNameEditText;

    /**
     * EditText field to enter the price
     */
    private EditText mPriceEditText;


    /**
     * EditText field to enter the quantity
     */
    private EditText mQuantityEditText;

    /**
     * Image View
     */
    private ImageView mInsertImage;

    private Button mPickImageButton;

    private static int IMG_RESULT = 1;

    Intent intent;


    private ContentValues mContenetvalues= null;

    private Uri mcurrentpeturi;

    private boolean mPetHasChanged = false;

    private Uri imageeUri;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHasChanged = true;
            return false;
        }
    };




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //get the on click list data
        Intent iintenet = getIntent();
        mcurrentpeturi = iintenet.getData();

        if (mcurrentpeturi == null) {
            setTitle("Add Product");
        }
        else {
          //  invalidateOptionsMenu();
            setTitle("Edit Product");
          //  getLoaderManager().initLoader(0, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_quantity);
        mInsertImage= (ImageView) findViewById(R.id.edit_imageView1);
        mPickImageButton = (Button) findViewById(R.id.edit_button1);


        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mInsertImage.setOnTouchListener(mTouchListener);


        //pick image from external source

        mPickImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                intent = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                startActivityForResult(intent, IMG_RESULT);

                                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), IMG_RESULT);

            }
        });
    }

    //when retreiving the image back from external source to set it on the view
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == IMG_RESULT && resultCode == RESULT_OK
                    && null != data) {


                imageeUri = data.getData();

                mInsertImage.setImageURI(imageeUri);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG)
                    .show();
        }
    }


    public void inserPets() {

        String nametext = mNameEditText.getText().toString().trim();
        String spricet= mPriceEditText.getText().toString().trim();
        String squantity= mQuantityEditText.getText().toString().trim();

        Integer pricetext=0, quantitytext=0;

        //Integer.parseInt(mPriceEditText.getText().toString().trim());
        //quantitytext = Integer.parseInt(mQuantityEditText.getText().toString().trim());

        //less than zero validations

        if (!TextUtils.isEmpty(spricet)) {
            pricetext=   Integer.parseInt(mPriceEditText.getText().toString().trim());
            if (pricetext < 0) {

                Toast.makeText(udEditorInv.this, "Less than zero value can not be accepted for price", Toast.LENGTH_SHORT).show();
            }
        }

        if (!TextUtils.isEmpty(squantity)) {
            quantitytext = Integer.parseInt(mQuantityEditText.getText().toString().trim());
            if (quantitytext < 0) {

                Toast.makeText(udEditorInv.this, "Less than zero value can not be accepted for quantity", Toast.LENGTH_SHORT).show();
            }
        }


        //blank values validations
        if (mcurrentpeturi == null &&(
                TextUtils.isEmpty(nametext) || TextUtils.isEmpty(spricet) ||
                TextUtils.isEmpty(squantity))||
                imageeUri==null) {
            Toast.makeText(udEditorInv.this, "Value can not be null, enter again ", Toast.LENGTH_SHORT).show();
        }


        mContenetvalues = new ContentValues();
        mContenetvalues.put(udConract.udConEntery.COLOUMN_NAME, nametext);
        mContenetvalues.put(udConract.udConEntery.COLOUMN_PRICE, pricetext);
        mContenetvalues.put(udConract.udConEntery.COLOUMN_QUANTITY, quantitytext);
        mContenetvalues.put(udConract.udConEntery.COLOUMN_IMAGE, imageeUri.toString());//image save as a Uri


        if (mcurrentpeturi == null) {
            Uri newUri = getContentResolver().insert(udConract.udConEntery.CONTENT_URI, mContenetvalues);
            if (newUri == null) {
                Toast.makeText(udEditorInv.this, "Error with saving product", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(udEditorInv.this, "New product saved", Toast.LENGTH_SHORT).show();
            }
        } else {
            int a = getContentResolver().update(mcurrentpeturi, mContenetvalues, null, null);

            if (a == 0) {
                Toast.makeText(udEditorInv.this, "Error with updating product", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(udEditorInv.this, "Product Data Updated", Toast.LENGTH_SHORT).show();
            }
        }
    }



    //shows the menu on top of activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mcurrentpeturi == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // insert pet info to DB
                inserPets();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
              //  showDeleteConfirmationDialog();

                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)

//                if (!mPetHasChanged) {
//                    NavUtils.navigateUpFromSameTask(this);
//                }

                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavUtils.navigateUpFromSameTask(udEditorInv.this);
                    }
                };

              //  showUnsavedChangesDialog(discardButtonClickListener);


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NavUtils.navigateUpFromSameTask(udEditorInv.this);

            }
        };

     //   showUnsavedChangesDialog(discardButtonClickListener);


    }

}
