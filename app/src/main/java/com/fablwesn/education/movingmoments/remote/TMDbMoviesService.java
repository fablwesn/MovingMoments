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

package com.fablwesn.education.movingmoments.remote;

import com.fablwesn.education.movingmoments.models.basic.MoviesContainer;
import com.fablwesn.education.movingmoments.models.details.MovieDetailsModel;
import com.fablwesn.education.movingmoments.models.details.reviews.ReviewsContainerDetails;
import com.fablwesn.education.movingmoments.models.details.videos.VideosContainerDetails;
import com.fablwesn.education.movingmoments.utility.ApiUtils;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Handles all Movie-related queries from the Web Api
 */
public interface TMDbMoviesService {

    // Get a list of the first page of result-movies with passed, pre-made category
    @GET("/3/movie/{category}")
    Call<MoviesContainer> getMoviesByCategory(
            @Path("category") String category,
            @Query(ApiUtils.API_URL_QUERY_PARAM_KEY) String apiKey,
            @Query(ApiUtils.API_URL_QUERY_PARAM_LANGUAGE) String language
    );

    // Get a movie-list of the first page of results from entered search query
    @GET("/3/search/movie")
    Call<MoviesContainer> getMoviesBySearch(
            @Query(ApiUtils.URL_QUERY_PARAM_API_KEY) String apiKey,
            @Query(ApiUtils.URL_QUERY_PARAM_LANGUAGE) String language,
            @Query(ApiUtils.URL_QUERY_PARAM_QUERY) String query
    );

    // Get list of the first page of result-movies with passed genre
    @GET("/3/movie/{movie_id}")
    Call<MovieDetailsModel> getMovieDetails(
            @Path("movie_id") int movie_id,
            @Query(ApiUtils.URL_QUERY_PARAM_API_KEY) String apiKey,
            @Query(ApiUtils.URL_QUERY_PARAM_LANGUAGE) String language,
            @Query(ApiUtils.URL_QUERY_PARAM_APPEND_RESPONSE) String appendix,
            @Query(ApiUtils.URL_QUERY_PARAM_IMG_LANG) String imgLang
    );

    // Get movies videos
    @GET("/3/movie/{movie_id}/videos")
    Call<VideosContainerDetails> getMovieVideos(
            @Path("movie_id") int movie_id,
            @Query(ApiUtils.URL_QUERY_PARAM_API_KEY) String apiKey,
            @Query(ApiUtils.URL_QUERY_PARAM_LANGUAGE) String language
    );

    // Get movies reviews
    @GET("/3/movie/{movie_id}/reviews")
    Call<ReviewsContainerDetails> getMovieReviews(
            @Path("movie_id") int movie_id,
            @Query(ApiUtils.URL_QUERY_PARAM_API_KEY) String apiKey,
            @Query(ApiUtils.URL_QUERY_PARAM_LANGUAGE) String language
    );
}
