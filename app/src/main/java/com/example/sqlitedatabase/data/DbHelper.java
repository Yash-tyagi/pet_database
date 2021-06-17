package com.example.sqlitedatabase.data;
import com.example.sqlitedatabase.data.PetContract.PetEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {


    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PetsData.db";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE "+PetEntry.TABLE_NAME+" ("+
            PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL,"+
            PetEntry.COLUMN_PET_BREED + " TEXT,"+
            PetEntry.COLUMN_PET_GENDER + " INTEGER,"+
            PetEntry.COLUMN_PET_WEIGHT + " INTEGER DEFAULT 0)";

    @Override
    public void onCreate(SQLiteDatabase db) {db.execSQL(SQL_CREATE_ENTRIES);}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
