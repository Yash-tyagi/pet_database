package com.example.sqlitedatabase.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class PetContract {

    private PetContract() {}

    public static abstract class PetEntry implements BaseColumns {
        public static final String TABLE_NAME = "pets";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PET_NAME = "name";
        public static final String COLUMN_PET_BREED = "breed";
        public static final String COLUMN_PET_GENDER = "gender";
        public static final String COLUMN_PET_WEIGHT = "weight";


        // constants for gender
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        // constants for content provider class (PetProvider)
        // A CONTENT URI looks like this ==> scheme------content_authority-------data(TableName)
        public static final String CONTENT_AUTHORITY= "com.example.sqlitedatabase";
        // Uri.parse take the Uri string and returns a Uri
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

        public static final String PATH_PETS = "pets";

        // Now the COMPLETE CONTENT URI
        // The Uri.withAppendedPath() method appends the BASE_CONTENT_URI (which contains the scheme and the content authority) to the path segment.
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PETS);

        /**
         * Returns whether or not the given gender is {@link #GENDER_UNKNOWN}, {@link #GENDER_MALE},
         * or {@link #GENDER_FEMALE}.
         */
        public static boolean isValidGender(int gender) {
            if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
                return true;
            }
            return false;
        }

    }

}
