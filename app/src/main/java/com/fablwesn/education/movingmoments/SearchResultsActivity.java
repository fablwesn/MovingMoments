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

package com.fablwesn.education.movingmoments;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fablwesn.education.movingmoments.adapters.MovieRecyclerAdapter;
import com.fablwesn.education.movingmoments.models.basic.MovieModel;
import com.fablwesn.education.movingmoments.models.basic.MoviesContainer;
import com.fablwesn.education.movingmoments.remote.TMDbMoviesService;
import com.fablwesn.education.movingmoments.utility.ApiUtils;
import com.fablwesn.education.movingmoments.utility.MiscUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Displays the results from a search query through the menu
 */
public class SearchResultsActivity extends AppCompatActivity {

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Fundamentals */

    @BindView(R.id.recycler_loading)
    RecyclerView recyclerView;
    @BindView(R.id.progressbar_loading)
    ProgressBar progressBar;
    @BindView(R.id.txt_empty_loading)
    TextView emptyText;

    private final static String KEY_LIST_STATE = "loaded_search_list"; // Key for the stored list
    private final static String KEY_USER_INPUT = "requested_query"; // Key for the stores user input

    private String userInput;
    private List<MovieModel> movieList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);

        // Takeover updating the variable from the previous activity to handle orientation changes correctly
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        HomeActivity.screenWidthPx = displayMetrics.widthPixels;

        // If a list has been loading and the activity has just been recreated (orientation change),
        // load the previously loaded list
        if (savedInstanceState != null) {
            movieList = savedInstanceState.getParcelableArrayList(KEY_LIST_STATE);
            userInput = savedInstanceState.getString(KEY_USER_INPUT, "");
            displayMovieList();
        }
        // Handle intent if a search has been initiated
        else {
            handleIntent(getIntent());
        }

        // Setup Up-Button and title in the toolbar.
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setTitle(getResources().getString(R.string.act_label_searchresults, userInput));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the currently loaded list
        if(movieList != null && !movieList.isEmpty()){
            outState.putParcelableArrayList(KEY_LIST_STATE, new ArrayList<Parcelable>(movieList));
        }
        outState.putString(KEY_USER_INPUT, userInput);
    }

    /*____________________________________________________________________________________________*/

    /* ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Menu */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // When the home button is pressed, take the user back
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*____________________________________________________________________________________________*/

    /* ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Helper methods */

    /**
     * Store the user input and initiate the search
     *
     * @param intent passed
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            userInput = intent.getStringExtra(SearchManager.QUERY);
            handleSearch();
        }
    }

    /**
     * Initiates the appropriate search.
     */
    private void handleSearch() {
        if (!MiscUtils.isNetworkAvailable(this)) {
            emptyText.setText(R.string.no_connection_error);
            return;
        } else {
            progressBar.setVisibility(View.VISIBLE);
            emptyText.setText(R.string.loading_data);
        }

        TMDbMoviesService tmdbService = ApiUtils.getTMDbMoviesService();

        Call<MoviesContainer> movieCall = tmdbService.getMoviesBySearch(
                ApiUtils.API_KEY,
                HomeActivity.deviceLanguage,
                userInput
        );
        movieCall.enqueue(new Callback<MoviesContainer>() {
            @Override
            public void onResponse(@NonNull Call<MoviesContainer> call, @NonNull Response<MoviesContainer> response) {
                MoviesContainer body = response.body();
                if (body != null && body.getResults() != null) {
                    movieList = body.getResults();
                    displayMovieList();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesContainer> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                emptyText.setText(R.string.no_data_fetched);
            }
        });
    }

    /**
     * Display the fetched movie data from the api
     */
    private void displayMovieList() {
        progressBar.setVisibility(View.GONE);

        if (movieList == null) {
            emptyText.setText(R.string.no_data_fetched);
        } else if (movieList.isEmpty()) {
            emptyText.setText(R.string.no_results_found);
        } else {
            emptyText.setText("");
            recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.results_list_grid_columns)));

            MovieRecyclerAdapter movieAdapter;
            movieAdapter = new MovieRecyclerAdapter(this, PreferenceManager.getDefaultSharedPreferences(this),
                    movieList);
            recyclerView.setAdapter(movieAdapter);
        }
    }

    /*____________________________________________________________________________________________*/
}
