package com.example.sqlitedatabase;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.sqlitedatabase.data.PetContract.PetEntry;

public class PetCursorAdapter extends CursorAdapter {

    private TextView mTvName;
    private TextView mTvBreed;
    private String name,breed;

    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_todo,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mTvName = view.findViewById(R.id.tvName);
        mTvBreed = view.findViewById(R.id.tvBreed);

        name = cursor.getString(cursor.getColumnIndexOrThrow(PetEntry.COLUMN_PET_NAME));
        breed = cursor.getString(cursor.getColumnIndexOrThrow(PetEntry.COLUMN_PET_BREED));
        Log.v("name",name);
        Log.v("breed",breed);
        // populate fields with extracted properties
        mTvName.setText(name);
        mTvBreed.setText(breed);
    }
}
