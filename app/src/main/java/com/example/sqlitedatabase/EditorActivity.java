package com.example.sqlitedatabase;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.sqlitedatabase.data.DbHelper;
import com.example.sqlitedatabase.data.PetContract.PetEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** EditText field to enter the pet's name */
    private EditText mNameEditText;

    /** EditText field to enter the pet's breed */
    private EditText mBreedEditText;

    /** EditText field to enter the pet's weight */
    private EditText mWeightEditText;

    /** EditText field to enter the pet's gender */
    private Spinner mGenderSpinner;

    /**
     * Gender of the pet. The possible valid values are in the PetContract.java file:
     * {@link PetEntry#GENDER_UNKNOWN}, {@link PetEntry#GENDER_MALE}, or
     * {@link PetEntry#GENDER_FEMALE}.
     */
    private int mGender = PetEntry.GENDER_UNKNOWN;
    private static final int EDITOR_LOADER_ID = 1;
    Uri mCurrentPetUri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentPetUri = intent.getData();

        if(mCurrentPetUri == null){
            setTitle(getString(R.string.add_a_pet));
        }else{
            setTitle(getString(R.string.edit_a_pet));
            getSupportLoaderManager().initLoader(EDITOR_LOADER_ID,null,this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById(R.id.edit_pet_name);
        mBreedEditText = findViewById(R.id.edit_pet_breed);
        mWeightEditText = findViewById(R.id.edit_pet_weight);
        mGenderSpinner = findViewById(R.id.spinner_gender);
        setupSpinner();
    }

    private void setupSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(adapter);

//        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selection = (String) parent.getItemAtPosition(position);
//
//                if (!TextUtils.isEmpty(selection)) {
//                    if (selection.equals(getString(R.string.gender_male))) {
//                        mGender = PetEntry.GENDER_MALE;
//                    } else if (selection.equals(getString(R.string.gender_female))) {
//                        mGender = PetEntry.GENDER_FEMALE;
//                    } else {
//                        mGender = PetEntry.GENDER_UNKNOWN;
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                mGender = PetEntry.GENDER_UNKNOWN;
//            }
//        });
    }

    private void insertPet(){

/**
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String breedString = mBreedEditText.getText().toString().trim();
        String weightString = mWeightEditText.getText().toString().trim();
        int weight = Integer.parseInt(weightString);
        /**
        // Create database helper
        DbHelper mDbHelper = new DbHelper(this);

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, nameString);
        values.put(PetEntry.COLUMN_PET_BREED, breedString);
        values.put(PetEntry.COLUMN_PET_GENDER, mGender);
        values.put(PetEntry.COLUMN_PET_WEIGHT, weight);
**/
        ContentValues values = getContentValues();


        Uri newUri = getContentResolver().insert(PetEntry.CONTENT_URI,values);

        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                    Toast.LENGTH_SHORT).show();
        }

        /**
        //Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(PetEntry.TABLE_NAME, null, values);

        if(newRowId == -1){
            Toast.makeText(this,"Error saving Pet details",Toast.LENGTH_SHORT).show();;
        }else{
            Toast.makeText(this,"[DATA] saved successfully :)",Toast.LENGTH_SHORT).show();;
        }
        **/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_save:
                if(mCurrentPetUri == null){
                    insertPet();
                }else {
                    editorActivityUpdate();
                }
                finish();
                return true;

            case R.id.action_delete:
                // do nothing for now
                return true;

            case R.id.home:
                // navigates back t parent activity
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT };
        return new CursorLoader(this,mCurrentPetUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // don't send data to someone just take all the matter and update the fields...
//        data.moveToFirst();
        if(data.moveToFirst()){
            int currentID = data.getInt(data.getColumnIndexOrThrow(PetEntry._ID));
            String currentName = data.getString(data.getColumnIndexOrThrow(PetEntry.COLUMN_PET_NAME));
            String currentBreed = data.getString(data.getColumnIndexOrThrow(PetEntry.COLUMN_PET_BREED));
            int currentGender = data.getInt(data.getColumnIndexOrThrow(PetEntry.COLUMN_PET_GENDER));
            int currentWeight = data.getInt(data.getColumnIndexOrThrow(PetEntry.COLUMN_PET_WEIGHT));

            mNameEditText.setText(currentName);
            mBreedEditText.setText(currentBreed);
//            mGenderSpinner.set
            mWeightEditText.setText(Integer.toString(currentWeight));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void editorActivityUpdate(){
        ContentValues values = getContentValues();

        int rowsAffected = getContentResolver().update(mCurrentPetUri,values,null,null);

        // Show a toast message depending on whether or not the update was successful.
        if (rowsAffected == 0) {
            // If no rows were affected, then there was an error with the update.
            Toast.makeText(this, getString(R.string.editor_update_pet_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_update_pet_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private ContentValues getContentValues(){
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String breedString = mBreedEditText.getText().toString().trim();
        String weightString = mWeightEditText.getText().toString().trim();
        int weight = Integer.parseInt(weightString);
        /**
         // Create database helper
         DbHelper mDbHelper = new DbHelper(this);

         // Gets the data repository in write mode
         SQLiteDatabase db = mDbHelper.getWritableDatabase();
         **/
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, nameString);
        values.put(PetEntry.COLUMN_PET_BREED, breedString);
        values.put(PetEntry.COLUMN_PET_GENDER, mGender);
        values.put(PetEntry.COLUMN_PET_WEIGHT, weight);

        return values;
    }

}
