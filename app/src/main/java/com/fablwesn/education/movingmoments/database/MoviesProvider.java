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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fablwesn.education.movingmoments.database.MoviesContract.MovieEntry;


/**
 * {@link ContentProvider} for the app.
 */
public class MoviesProvider extends ContentProvider {

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Globals */

    // Tag used for logging.
    private static final String LOG_TAG = MoviesProvider.class.getSimpleName();

    // used for building the selection working with specific entries by id 
    private static final String MOVIE_ID_SECTION_APPENDIX = "=?";

    // No context found. */
    public final static String PROVIDER_NO_CONTEXT = "NullPointer Exception! No Context found.";

    // insert errors inside the provider. 
    public final static String PROVIDER_INSERT_URI_ERROR = "Insertion is not supported for the following URI: ";
    public final static String PROVIDER_INSERT_ROW_ERROR = "Failed to insert row inside URI: ";

    // query errors inside the provider. 
    public final static String PROVIDER_QUERY_NO_MATCH = "Cannot query unknown URI ";

    // update errors inside the provider. 
    public final static String PROVIDER_UPDATE_URI_ERROR = "Updating is not supported for the following URI: ";

    // delete errors inside the provider. 
    public final static String PROVIDER_DELETE_URI_ERROR = "Deleting impossible inside URI: ";

    // getType errors inside the provider.
    public final static String PROVIDER_GET_TYPE_URI_ERROR = "Unknown URI: ";
    public final static String PROVIDER_GET_TYPE_MATCH_ERROR = " with match: ";

    // Database helper object
    private MovieDbHelper dbHelper;

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Fundamentals */

    /**
     * OnCreate:
     *
     * @return true after initializing the db helper object.
     */
    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return true;
    }

    /**
     * Handles requests for the MIME type.
     *
     * @param uri to query
     * @return the MIME type of data in the content provider
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // check for match and act accordingly
        final int match = URI_MATCHER.match(uri);
        switch (match) {
            case MOVIES:
                return MovieEntry.CONTENT_LIST_TYPE;
            case MOVIE_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException(PROVIDER_GET_TYPE_URI_ERROR + uri + PROVIDER_GET_TYPE_MATCH_ERROR + match);
        }
    }

    /**
     * Insert new data into the database with the given content values into the correct table.
     *
     * @param uri           where to insert
     * @param contentValues what to insert
     * @return new content URI for that specific row in the database.
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = URI_MATCHER.match(uri);
        switch (match) {
            case MOVIES:
                return insertMovie(MOVIES, uri, contentValues);
            default:
                throw new IllegalArgumentException(PROVIDER_INSERT_URI_ERROR + uri);
        }
    }

    /**
     * Method to read from the database.
     *
     * @param uri           to query from
     * @param projection    the columns wanted
     * @param selection     where to look
     * @param selectionArgs arguments of where, preventing SQL injection
     * @param sortOrder     order in which the returned results should appear
     * @return Cursor object containing the requested data
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // Get readable database
        SQLiteDatabase sqlDatabase = dbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher matches the URI to a specific code and act accordingly
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case MOVIES:
                // query the whole movies table.
                cursor = sqlDatabase.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case MOVIE_ID:
                // set the correct selection argument
                selection = MovieEntry.COLUMN_TMDB_ID + MOVIE_ID_SECTION_APPENDIX;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // query the single movie in question from the movies table.
                cursor = sqlDatabase.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException(PROVIDER_QUERY_NO_MATCH + uri);
        }

        // Set notification URI on the Cursor
        if (getContext() != null)
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        else
            throw new NullPointerException(PROVIDER_NO_CONTEXT);

        // Return the cursor
        return cursor;
    }

    /**
     * Update data in the database with the given content values.
     *
     * @param uri           of where to update
     * @param values        new values to add
     * @param selection     where clause
     * @param selectionArgs arguments of the where clause
     * @return int of rows that have been updated
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // look for a match and act accordingly
        final int match = URI_MATCHER.match(uri);
        switch (match) {
            case MOVIES:
                return updateMovie(MOVIES, uri, values, selection, selectionArgs);
            case MOVIE_ID:
                selection = MovieEntry.COLUMN_TMDB_ID + MOVIE_ID_SECTION_APPENDIX;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateMovie(MOVIES, uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(PROVIDER_UPDATE_URI_ERROR + uri);
        }
    }

    /**
     * Delete multiple or single items from the movies table
     *
     * @param uri           pointing to the movie(s) to be deleted
     * @param selection     where to look
     * @param selectionArgs arguments for the where clause
     * @return int of rows deleted
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        // Get writable database
        SQLiteDatabase sqlDatabase = dbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        // Look if the uri is valid and act accordingly.
        final int match = URI_MATCHER.match(uri);
        switch (match) {
            case MOVIES:
                // Delete all rows that match the selection and selection args
                rowsDeleted = sqlDatabase.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_ID:
                // Delete a single row given by the ID in the URI
                selection = MovieEntry.COLUMN_TMDB_ID + MOVIE_ID_SECTION_APPENDIX;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = sqlDatabase.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException(PROVIDER_DELETE_URI_ERROR + uri);
        }

        // If one or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            if (getContext() != null)
                getContext().getContentResolver().notifyChange(uri, null);
            else
                throw new NullPointerException(PROVIDER_NO_CONTEXT);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }
    
    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      URI Matcher */

    // URI matcher code for the content uri for the movies table.
    private static final int MOVIES = 201;
    // URI matcher code for the content uri for a single movie from the movies table.
    private static final int MOVIE_ID = 202;
    // Placeholder for the id
    private static final String PATH_ID_PLACEHOLDER = "/#";

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     */
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    /*
     * Initializer.
     */
    static {
        // add single and all movies to the matcher
        URI_MATCHER.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        URI_MATCHER.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES + PATH_ID_PLACEHOLDER, MOVIE_ID);
    }

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Helper Methods */

    /**
     * Helper method for adding a row inside a table
     *
     * @param table  which to add to
     * @param uri    of the table
     * @param values of data to be inserted
     * @return Uri object pointing to the row added
     */
    private Uri insertMovie(int table, Uri uri, ContentValues values) {
        // Get writable database
        SQLiteDatabase sqlDatabase = dbHelper.getWritableDatabase();

        // id for the newly created row
        long newId;

        // insert into the correct table
        switch (table) {
            case MOVIES:
                newId = sqlDatabase.insert(MovieEntry.TABLE_NAME, null, values);
                break;
            default:
                // -1 refers to an error
                newId = -1;
                break;
        }

        // check if there was an error adding the data
        if (newId == -1) {
            Log.e(LOG_TAG, PROVIDER_INSERT_ROW_ERROR + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the content URI
        if (getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
        else
            throw new NullPointerException(PROVIDER_NO_CONTEXT);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, newId);
    }

    /**
     * Helper method for updating a row in database
     *
     * @param uri           of the movie
     * @param values        new values
     * @param selection     the where clause
     * @param selectionArgs arguments of the where clause
     * @return int of rows updated
     */
    private int updateMovie(int table, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //Return early if no data is changed
        if (values.size() == 0) {
            return 0;
        }

        // get the db
        SQLiteDatabase sqlDatabase = dbHelper.getWritableDatabase();

        // will store the number of rows updated
        int rowsUpdated;

        // choose correct table
        switch (table) {
            case MOVIES:
                rowsUpdated = sqlDatabase.update(MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                rowsUpdated = 0;
                break;
        }

        // if there was a change, notify
        if (rowsUpdated != 0) {
            if (getContext() != null)
                getContext().getContentResolver().notifyChange(uri, null);
            else
                throw new NullPointerException(PROVIDER_NO_CONTEXT);
        }

        // return number of rows updated
        return rowsUpdated;
    }

    /*____________________________________________________________________________________________*/
}
