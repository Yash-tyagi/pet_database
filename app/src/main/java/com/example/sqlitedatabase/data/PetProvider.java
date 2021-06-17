package com.example.sqlitedatabase.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.os.IResultReceiver;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.sqlitedatabase.data.PetContract.PetEntry;

public class PetProvider extends ContentProvider {

    private static final int PETS = 1;
    private static final int PET_ID = 2;
    public static final String LOG_TAG = PetProvider.class.getSimpleName();

    // URI MATCHER
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(PetEntry.CONTENT_AUTHORITY,PetEntry.PATH_PETS,PETS);
        sUriMatcher.addURI(PetEntry.CONTENT_AUTHORITY,PetEntry.PATH_PETS+"/#",PET_ID);
    }

    private DbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match){
            case PETS:
                cursor = db.query(PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case PET_ID:
                selection = PetEntry._ID+"=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;

            default:
                throw new IllegalArgumentException("cannot query unknown uri "+uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
//        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values){

        String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        // Check that the gender is valid
        Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
        if (gender == null || !PetEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Pet requires valid gender");
        }

        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);

        switch (match){
            case PETS:
//                return insertPet(uri,values);
                long id = db.insert(PetEntry.TABLE_NAME,null,values);
                // checking for the id...
                if (id == -1) {
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                    return null;
                }

                // in observer if we fill null then CursorApdater will automatically get notified...
                // so that means the loader callbacks will still be automatically triggered
                getContext().getContentResolver().notifyChange(uri,null);

                return ContentUris.withAppendedId(uri,id);

            default:

                throw new IllegalArgumentException("cannot query unknown uri "+uri);
        }
    }


    int rowsDeleted;
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        switch (match){
            case PETS:
                rowsDeleted = db.delete(PetEntry.TABLE_NAME,selection,selectionArgs);


            case PET_ID:
                // Delete a single row given by the ID in the URI
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = db.delete(PetEntry.TABLE_NAME, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("cannot delete unknown uri "+uri);
        }
//        if ( rowsDeleted != 0) {
//            getContext().getContentResolver().notifyChange(uri, null);
//        }

//        return  rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int match = sUriMatcher.match(uri);

        switch (match){
            case PETS:
                return updatePet(uri,values,selection,selectionArgs);
            case PET_ID:

                selection = PetEntry._ID+" =?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri,values,selection,selectionArgs);

            default:

        }

        return 0;
    }

    private int updatePet(Uri uri,ContentValues values, String selection, String[] selectionArgs){

        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(PetEntry.COLUMN_PET_NAME)) {
            String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(PetEntry.COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
            if (gender == null || !PetEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(PetEntry.COLUMN_PET_WEIGHT)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowsUpdated = db.update(PetEntry.TABLE_NAME,values,selection,selectionArgs);
        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

}
