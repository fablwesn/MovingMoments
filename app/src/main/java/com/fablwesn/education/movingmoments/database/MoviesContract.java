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

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * DB Contract for the app.
 */
public class MoviesContract {

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Base Contract */

    // Name for the content provider.
    static final String CONTENT_AUTHORITY = "com.fablwesn.education.movingmoments";

    // base of all URI's to contact the provider
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // path containing the the table with the movies stored
    // (content://com.fablwesn.education.movingmoments/movies)
    static final String PATH_MOVIES = "movies";

    /**
     * Empty constructor preventing accidental instantiation.
     */
    private MoviesContract() {
    }

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Movie Table */

    /**
     * Inner class that defines constant values for the movies database table.
     * Each entry in the table represents a favorited movie.
     */
    public static final class MovieEntry implements BaseColumns {

        // The content URI to access the movie data in the provider.
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);

        // The MIME type of the CONTENT_URI for a list of movies.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE
                        + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        // The MIME type of the CONTENT_URI for a single movie.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE
                        + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        // Name of database table for movies.
        public final static String TABLE_NAME = "movies";


        /*
           table columns */

        public final static String DB_ID = BaseColumns._ID;

        public static final String COLUMN_TMDB_ID = "tmdb_id";

        public static final String COLUMN_ADULT = "adult";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_RELEASE = "release_date";

        public static final String COLUMN_GENRE = "genres";

        public static final String COLUMN_ORIG_TITLE = "original_title";

        public static final String COLUMN_ORIG_LANG = "original_language";

        public static final String COLUMN_VOTE_COUNT = "vote_count";

        public static final String COLUMN_VOTE_AVG = "vote_average";

        public static final String DELETE_CLAUSE_ID = COLUMN_TMDB_ID + "=?";
    }

    /*____________________________________________________________________________________________*/

}
