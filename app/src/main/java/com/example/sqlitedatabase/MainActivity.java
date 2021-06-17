package com.example.sqlitedatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.app.*;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sqlitedatabase.data.DbHelper;
import com.example.sqlitedatabase.data.PetContract.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static java.security.AccessController.getContext;

//import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID=0;
    PetCursorAdapter mPetCursorAdapter;

    private DbHelper mDbHelper;
    private TextView mDisplayView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup fab to open editor activity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new DbHelper(this);
        mListView = findViewById(R.id.list_view);

        View empty_view = findViewById(R.id.empty_view);
        mListView.setEmptyView(empty_view);

        mPetCursorAdapter = new PetCursorAdapter(this,null);
        mListView.setAdapter(mPetCursorAdapter);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,EditorActivity.class);
                Uri currentPetUri = ContentUris.withAppendedId(PetEntry.CONTENT_URI,id);

                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });

    }


    void insertPet(){
        // do nothing in the mean while
    }
/**
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT };

        // Perform a query on the pets table
         Cursor cursor = db.query(
                PetEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        Cursor cursor = getContentResolver().query(PetEntry.CONTENT_URI,projection,null,null,null);
        // creating an object of Cursor Adapter;
        PetCursorAdapter petCursorAdapter = new PetCursorAdapter(this,cursor);

        try {
            // Create a header in the Text View that looks like this:
            //
            // The pets table contains <number of rows in Cursor> pets.
            // _id - name - breed - gender - weight
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.

            mDisplayView.setText("The pets table contains " + cursor.getCount() + " pets.\n\n");
            mDisplayView.append(PetEntry._ID + " - " +
                    PetEntry.COLUMN_PET_NAME + " - " +
                    PetEntry.COLUMN_PET_BREED + " - " +
                    PetEntry.COLUMN_PET_GENDER + " - " +
                    PetEntry.COLUMN_PET_WEIGHT + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(PetEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
            int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
            int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
            int weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentBreed = cursor.getString(breedColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                int currentWeight = cursor.getInt(weightColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                mDisplayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentBreed + " - " +
                        currentGender + " - " +
                        currentWeight));
            }


            // linking view to the adapter

        } catch (Exception e){
            Log.v("ERROR HAI BHAI LOG",e.getMessage());
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

        mListView.setAdapter(petCursorAdapter);

    }
**/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // user clicked on the option menu bar overflow menu

        switch (item.getItemId()){

            case R.id.action_insert_dummy_data:
                insertPet();
                return true;

            case R.id.action_delete_all_entries:
                // do nothing for now
//                displayDatabaseInfo();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    // call back functions for loader
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT };

//        Cursor cursor = getContentResolver().query(PetEntry.CONTENT_URI,projection,null,null,null);

        return new CursorLoader(
                this,
                PetEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // Update PetCursor Adapter with this new cursor containing updated pet data
        mPetCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // this callback is called when the data needs to be deleted....
        mPetCursorAdapter.swapCursor(null);
    }
}