package com.kridacreations.diary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DiaryDbHelper extends SQLiteOpenHelper {
    /** Name of the database file */
    private static final String DATABASE_NAME = "diary.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link DiaryDbHelper}.
     *
     * @param context of the app
     */
    public DiaryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INFO_TABLE =  "CREATE TABLE " + DiaryContract.InfoEntry.INFO_TABLE_NAME + " ("
                + DiaryContract.InfoEntry.INFO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DiaryContract.InfoEntry.INFO_COLUMN_NAME + " TEXT NOT NULL, "
                + DiaryContract.InfoEntry.INFO_COLUMN_PIN + " TEXT NOT NULL)";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_INFO_TABLE);

        String SQL_CREATE_DAYS_TABLE =  "CREATE TABLE " + DiaryContract.DaysEntry.DAYS_TABLE_NAME + " ("
                + DiaryContract.DaysEntry.DAYS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DiaryContract.DaysEntry.DAYS_COLUMN_DATE + " INTEGER NOT NULL, "
                + DiaryContract.DaysEntry.DAYS_COLUMN_MONTH + " INTEGER NOT NULL, "
                + DiaryContract.DaysEntry.DAYS_COLUMN_YEAR + " INTEGER NOT NULL, "
                + DiaryContract.DaysEntry.DAYS_COLUMN_FEEL + " TEXT NOT NULL, "
                + DiaryContract.DaysEntry.DAYS_COLUMN_DESC + " TEXT NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_DAYS_TABLE);

        Log.v("dbhelper", "tabel \n create \n agdshg");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
