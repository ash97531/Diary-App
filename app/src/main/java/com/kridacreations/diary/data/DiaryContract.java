package com.kridacreations.diary.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DiaryContract {

    private DiaryContract(){}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.kridacreations.android.diary";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.diary/info/ is a valid path for
     * looking at pet data. content://com.example.android.diary/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_INFO = "INFO";
    public static final String PATH_DAYS = "days";

    public static final class InfoEntry implements BaseColumns{
        /**
         * The MIME type of the {@link #INFO_CONTENT_URI} for a list of pets.
         */
        public static final String INFO_CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INFO;

        /**
         * The MIME type of the {@link #INFO_CONTENT_URI} for a single pet.
         */
        public static final String INFO_CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INFO;

        /** The content URI to access the pet data in the provider */
        public static final Uri INFO_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INFO);

        public static final String INFO_TABLE_NAME = "info";

        public static final String INFO_ID = "id";
        public static final String INFO_COLUMN_NAME = "name";
        public static final String INFO_COLUMN_PIN = "pin";
    }

    public static final class DaysEntry implements BaseColumns{
        /**
         * The MIME type of the {@link #DAYS_CONTENT_URI} for a list of pets.
         */
        public static final String DAYS_CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DAYS;

        /**
         * The MIME type of the {@link #DAYS_CONTENT_URI} for a single pet.
         */
        public static final String DAYS_CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DAYS;

        /** The content URI to access the pet data in the provider */
        public static final Uri DAYS_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DAYS);

        public static final String DAYS_TABLE_NAME = "days";

        public static final String DAYS_ID = "id";
        public static final String DAYS_COLUMN_DATE = "date";
        public static final String DAYS_COLUMN_MONTH= "month";
        public static final String DAYS_COLUMN_YEAR= "year";
        public static final String DAYS_COLUMN_FEEL= "feel";
        public static final String DAYS_COLUMN_DESC= "description";

    }

}
