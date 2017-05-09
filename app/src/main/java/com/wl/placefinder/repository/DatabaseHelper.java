package com.wl.placefinder.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.wl.placefinder.model.QueryModel;

import java.util.List;
import java.util.ArrayList;

/**
 * This class helps open, create, and upgrade the database file.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Search Query table information class.
     */
    public static final class SearchQueryTable {
        /**
         * The table name of available query list.
         */
        public static final String TABLE_NAME = "search_queries";

        /**
         * Column name for startSearch query.
         * <P>Type: TEXT</P>
         */
        public static final String COLUMN_NAME_QUERY = "query";

        /**
         * Query statement for creating product table.
         */
        public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " ( " +
                COLUMN_NAME_QUERY + " TEXT PRIMARY KEY );";
    }

    /**
     * Database name.
     */
    private static final String DATABASE_NAME = "place_finder.db";

    /**
     * Database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor.
     *
     * @param context context.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SearchQueryTable.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public List<QueryModel> getAllQueries() {
        ArrayList<QueryModel> queries = new ArrayList<QueryModel>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            cursor = sqLiteDatabase.query(SearchQueryTable.TABLE_NAME, null, null, null, null, null, SearchQueryTable.COLUMN_NAME_QUERY);
            if (null != cursor) {

                while (cursor.moveToNext()) {
                    String query = cursor.getString(cursor.getColumnIndex(SearchQueryTable.COLUMN_NAME_QUERY));

                    QueryModel queryModel = new QueryModel(query);
                    queries.add(queryModel);
                }
                cursor.close();
                cursor = null;
            }
            sqLiteDatabase.close();
            sqLiteDatabase = null;
        } catch (SQLiteException e) {
        } finally {
            if (null != cursor) {
                cursor.close();
            }
            if (null != sqLiteDatabase) {
                sqLiteDatabase.close();
            }
        }

        return queries;

    }

    public boolean insert(QueryModel queryModel) {
        SQLiteDatabase sqLiteDatabase = null;

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SearchQueryTable.COLUMN_NAME_QUERY, queryModel.getQuery());

            sqLiteDatabase = getWritableDatabase();
// TODO: 5/9/2017 avoid duplicates being primary key
            long rowId = sqLiteDatabase.insert(SearchQueryTable.TABLE_NAME, null, contentValues);
            if (-1 == rowId) {
                return false;
            }

            sqLiteDatabase.close();
            sqLiteDatabase = null;
        } catch (SQLiteException e) {
        } finally {
            if (null != sqLiteDatabase) {
                sqLiteDatabase.close();
            }
        }

        return true;
    }

}
