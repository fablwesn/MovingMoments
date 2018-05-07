/*
 * Copyright (C) 2018 Darijo Barucic, Seventoes
 *
 *  Licensed under the MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.fablwesn.education.movingmoments.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fablwesn.education.movingmoments.database.MoviesContract.MovieEntry;

/**
 * Database helper for the app. Manages database creation and version management.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Globals */

    // Tag used for logging.
    private static final String LOG_TAG = MovieDbHelper.class.getSimpleName();

    // Name of the database file.
    private static final String DATABASE_NAME = "mm_favorites.db";

    // Database version. If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    // Basic SQL-Command chars
    private static final String SQL_COMMAND_SEPARATOR = ",";
    private static final String SQL_COMMAND_START_SCHEME = " (";
    private static final String SQL_COMMAND_END_SCHEME = ");";

    // SQL Command to create a table
    private static final String SQL_COMMAND_CREATE_TABLE = "CREATE TABLE ";

    // SQL Command Types
    private static final String SQL_COMMAND_TYPE_INTEGER = " INTEGER ";
    private static final String SQL_COMMAND_TYPE_TEXT = " TEXT ";
    private static final String SQL_COMMAND_TYPE_REAL = " REAL ";

    // SQL Command Primary Key
    private static final String SQL_COMMAND_PRIMARY_KEY = " PRIMARY KEY";
    // SQL Command Auto Increment
    private static final String SQL_COMMAND_INCREMENT = "AUTO_INCREMENT";
    // SQL Command not null condition
    private static final String SQL_COMMAND_NOT_NULL = "NOT NULL";

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Fundamentals */

    /**
     * Constructs a new instance of {@link MovieDbHelper}.
     *
     * @param context of the app
     */
    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the command.
        final String sqlCommand = createSqlTablesCommand();
        // Execute the SQL statements to create the table.
        db.execSQL(sqlCommand);
        // Log the command for debugging
        Log.v(LOG_TAG, sqlCommand);
    }

    /**
     * Called when the database scheme needs to be updated.
     * IMPORTANT: increment constant DATABASE_VERSION before updating!!
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // nothing yet
    }

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Helper Methods */

    /**
     * @return String containing SQLite Command creating the FAVORITE MOVIES table.
     */
    private String createSqlTablesCommand() {

        return SQL_COMMAND_CREATE_TABLE + MovieEntry.TABLE_NAME + SQL_COMMAND_START_SCHEME
                + MovieEntry.DB_ID + SQL_COMMAND_TYPE_INTEGER + SQL_COMMAND_INCREMENT + SQL_COMMAND_SEPARATOR
                + MovieEntry.COLUMN_TMDB_ID + SQL_COMMAND_TYPE_INTEGER + SQL_COMMAND_NOT_NULL + SQL_COMMAND_PRIMARY_KEY + SQL_COMMAND_SEPARATOR
                + MovieEntry.COLUMN_ADULT + SQL_COMMAND_TYPE_TEXT + SQL_COMMAND_SEPARATOR
                + MovieEntry.COLUMN_TITLE + SQL_COMMAND_TYPE_TEXT + SQL_COMMAND_SEPARATOR
                + MovieEntry.COLUMN_POSTER_PATH + SQL_COMMAND_TYPE_TEXT + SQL_COMMAND_SEPARATOR
                + MovieEntry.COLUMN_BACKDROP_PATH + SQL_COMMAND_TYPE_TEXT + SQL_COMMAND_SEPARATOR
                + MovieEntry.COLUMN_OVERVIEW + SQL_COMMAND_TYPE_TEXT + SQL_COMMAND_SEPARATOR
                + MovieEntry.COLUMN_RELEASE + SQL_COMMAND_TYPE_TEXT + SQL_COMMAND_SEPARATOR
                + MovieEntry.COLUMN_GENRE + SQL_COMMAND_TYPE_TEXT + SQL_COMMAND_SEPARATOR
                + MovieEntry.COLUMN_ORIG_TITLE + SQL_COMMAND_TYPE_TEXT + SQL_COMMAND_SEPARATOR
                + MovieEntry.COLUMN_ORIG_LANG + SQL_COMMAND_TYPE_TEXT + SQL_COMMAND_SEPARATOR
                + MovieEntry.COLUMN_VOTE_COUNT + SQL_COMMAND_TYPE_INTEGER + SQL_COMMAND_SEPARATOR
                + MovieEntry.COLUMN_VOTE_AVG + SQL_COMMAND_TYPE_REAL
                + SQL_COMMAND_END_SCHEME;
    }

    /*____________________________________________________________________________________________*/
}
