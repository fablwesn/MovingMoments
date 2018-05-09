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

package com.fablwesn.education.movingmoments.utility;

import android.content.Context;
import android.util.DisplayMetrics;

import com.fablwesn.education.movingmoments.remote.RetrofitClient;
import com.fablwesn.education.movingmoments.remote.TMDbMoviesService;

/**
 * Includes keys used for querying and parsing from the api
 * and helper methods for the same purpose.
 */
public class ApiUtils {

    public static final String API_KEY = ""; // TODO: ADD YOUR API-KEY HERE

    public static final String MOVIE_TMDB_LINK_BASE = "https://www.themoviedb.org/movie/";
    public static final String MOVIE_TMDB_LINK_LANG = "?language=";
    public static final String API_CATEGORY_POPULAR = "popular";
    public static final String API_CATEGORY_TOP_RATED = "top_rated";
    public static final String API_URL_QUERY_PARAM_KEY = "api_key";
    public static final String API_URL_QUERY_PARAM_LANGUAGE = "language";
    public static final String URL_QUERY_PARAM_API_KEY = "api_key";
    public final static String URL_QUERY_PARAM_QUERY = "query";
    public static final String URL_QUERY_PARAM_LANGUAGE = "language";

    public final static String URL_QUERY_PARAM_APPEND_RESPONSE = "append_to_response";
    public final static String URL_QUERY_VALUE_TO_APPEND = "images,release_dates";
    public final static String URL_QUERY_PARAM_IMG_LANG = "include_image_language";
    public final static String URL_QUERY_VALUE_IMG_LANG = "null";

    public static final String BASE_IMAGES_URL = "https://image.tmdb.org/t/p/";

    private final static String RAW_QUERY_URL_POSTER_SIZE_SMALL = "w185/";
    private final static String RAW_QUERY_URL_POSTER_SIZE_MEDIUM = "w342/";
    private final static String RAW_QUERY_URL_POSTER_SIZE_LARGE = "w500/";

    private final static String RAW_QUERY_URL_BACKDROP_SIZE_SMALL = "w300/";
    private final static String RAW_QUERY_URL_BACKDROP_SIZE_MEDIUM = "w780/";
    private final static String RAW_QUERY_URL_BACKDROP_SIZE_LARGE = "w1280/";

    public final static String YOUTUBE_APP_INTENT_URI = "vnd.youtube:";
    public final static String YOUTUBE_WEB_INTENT_URI = "https://www.youtube.com/watch?v=";
    public final static String YOUTUBE_THUMBNAIL_START = "https://img.youtube.com/vi/";
    public final static String YOUTUBE_THUMBNAIL_END = "/default.jpg";

    /**
     * @return new or existing retrofit2 client for movie requests
     */
    public static TMDbMoviesService getTMDbMoviesService() {
        return RetrofitClient.getClient().create(TMDbMoviesService.class);
    }

    /**
     * Chose an poster-image-size to query depending on the user's device resolution.
     *
     * @param context of the activity
     * @return correct path-part for the image's size
     */
    public static String getPosterSizePath(Context context) {
        switch (context.getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return RAW_QUERY_URL_POSTER_SIZE_SMALL;
            case DisplayMetrics.DENSITY_XHIGH:
            case DisplayMetrics.DENSITY_XXHIGH:
            case DisplayMetrics.DENSITY_XXXHIGH:
                return RAW_QUERY_URL_POSTER_SIZE_LARGE;
            case DisplayMetrics.DENSITY_MEDIUM:
            case DisplayMetrics.DENSITY_HIGH:
            default:
                return RAW_QUERY_URL_POSTER_SIZE_MEDIUM;
        }
    }

    /**
     * Chose an backdrop-image-size to query depending on the user's device resolution.
     *
     * @param context of the activity
     * @return correct path-part for the image's size
     */
    public static String getBackdropSizePath(Context context) {
        switch (context.getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return RAW_QUERY_URL_BACKDROP_SIZE_SMALL;
            case DisplayMetrics.DENSITY_XHIGH:
            case DisplayMetrics.DENSITY_XXHIGH:
            case DisplayMetrics.DENSITY_XXXHIGH:
                return RAW_QUERY_URL_BACKDROP_SIZE_LARGE;
            case DisplayMetrics.DENSITY_MEDIUM:
            case DisplayMetrics.DENSITY_HIGH:
            default:
                return RAW_QUERY_URL_BACKDROP_SIZE_MEDIUM;
        }
    }
}
