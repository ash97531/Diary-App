package com.kridacreations.diary.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DaysProvider extends ContentProvider {
    private DiaryDbHelper mDbHelper;

    /** URI matcher code for the content URI for the pets table */
    private static final int DAYS = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int DAY_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        sUriMatcher.addURI(DiaryContract.CONTENT_AUTHORITY, DiaryContract.PATH_DAYS, DAYS);
        sUriMatcher.addURI(DiaryContract.CONTENT_AUTHORITY, DiaryContract.PATH_DAYS + "/#", DAY_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DiaryDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case DAYS:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = database.query(DiaryContract.DaysEntry.DAYS_TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null);
                break;
            case DAY_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = DiaryContract.DaysEntry.DAYS_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(DiaryContract.DaysEntry.DAYS_TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DAYS:
                return DiaryContract.DaysEntry.DAYS_CONTENT_LIST_TYPE;
            case DAY_ID:
                return DiaryContract.DaysEntry.DAYS_CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sUriMatcher.match(uri);
        switch (match){
            case DAYS:
                return insertDay(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertDay(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(DiaryContract.DaysEntry.DAYS_TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.v("pet provider", "Failed to insert row for " + uri);
            return null;
        }

        Log.v("pet provider", "success \n to insert row for " + uri);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DAYS:
                // Delete all rows that match the selection and selection args
                return database.delete(DiaryContract.DaysEntry.DAYS_TABLE_NAME, selection, selectionArgs);
            case DAY_ID:
                selection = DiaryContract.DaysEntry.DAYS_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                Log.v("main activity", selectionArgs[0] + "ascasc \n dasgvsd");

                return database.delete(DiaryContract.DaysEntry.DAYS_TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DAYS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case DAY_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = DiaryContract.DaysEntry.DAYS_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }

    private int updatePet(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        int row = db.update(DiaryContract.DaysEntry.DAYS_TABLE_NAME, contentValues, selection, selectionArgs);

        return row;
    }
}
