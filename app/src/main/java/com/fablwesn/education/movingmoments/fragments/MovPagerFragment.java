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

package com.fablwesn.education.movingmoments.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fablwesn.education.movingmoments.HomeActivity;
import com.fablwesn.education.movingmoments.R;
import com.fablwesn.education.movingmoments.adapters.MovieRecyclerAdapter;
import com.fablwesn.education.movingmoments.models.basic.MovieModel;
import com.fablwesn.education.movingmoments.models.basic.MoviesContainer;
import com.fablwesn.education.movingmoments.remote.TMDbMoviesService;
import com.fablwesn.education.movingmoments.utility.ApiUtils;
import com.fablwesn.education.movingmoments.utility.MiscUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Used as for the pager to display a list of movies {@link com.fablwesn.education.movingmoments.adapters.MovPagerAdapter}
 */
public class MovPagerFragment extends Fragment {

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Bindings */

    @BindView(R.id.refresh_lay_fragment)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view_fragment)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar_fragment)
    ProgressBar progressBar;
    @BindView(R.id.text_empty_fragment)
    TextView emptyText;

    @BindString(R.string.no_connection_error)
    String noConnectionString;

    /* __________________________________________________________________________________________ */

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Globals */

    private static final String KEY_PAGER_TITLE = "page_title_category";

    private Activity activity;
    private Unbinder viewUnbind;
    private String categoryDisplaying;
    private List<MovieModel> movieList = null;

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Fundamentals */

    /**
     * Create the fragment and save the page title of the fragment.
     *
     * @return pageFragment to add to the pager.
     */
    public static MovPagerFragment newInstance(String title) {
        MovPagerFragment pageFragment = new MovPagerFragment();
        Bundle bundle = new Bundle();

        // Save the tab page title.
        bundle.putString(KEY_PAGER_TITLE, title);
        pageFragment.setArguments(bundle);

        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        // Find out which category this page needs to display
        Bundle bundle = getArguments();
        if (bundle != null) {
            categoryDisplaying = getArguments().getString(KEY_PAGER_TITLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        View rootView = inflater.inflate(R.layout.frag_pager_recycler, container, false);
        viewUnbind = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        prepareSwipeRefresh();

        // If we already have loaded a list, display it instead of querying again
        if (savedInstanceState != null) {
            movieList = savedInstanceState.getParcelableArrayList(categoryDisplaying);
        }

        if (movieList != null) {
            displayList();
        } else {
            if(MiscUtils.isNetworkAvailable(activity)){
                queryApiList();
            } else {
                progressBar.setVisibility(View.GONE);
                emptyText.setText(noConnectionString);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Free binded views.
        viewUnbind.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Store the loaded movie, if available
        if (movieList != null && !movieList.isEmpty()) {
            outState.putParcelableArrayList(categoryDisplaying, new ArrayList<Parcelable>(movieList));
        }
    }

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Helper Methods */

    /**
     * Sets an onRefreshListener for the user to update the list
     */
    private void prepareSwipeRefresh() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (MiscUtils.isNetworkAvailable(activity)) {
                    queryApiList();
                } else if (movieList != null && !movieList.isEmpty()) {
                    Toast.makeText(activity, noConnectionString, Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                } else {
                    emptyText.setText(noConnectionString);
                    refreshLayout.setRefreshing(false);
                }
            }
        });
    }

    /**
     * Queries the TMDB-API using {@link retrofit2.Retrofit} depending on the category displayed.
     */
    private void queryApiList() {
        // Get our remote service
        TMDbMoviesService tmdbService = ApiUtils.getTMDbMoviesService();

        // Check if to load popular or top rated
        String category;
        if (categoryDisplaying.equals(getString(R.string.title_category_top))) {
            category = ApiUtils.API_CATEGORY_TOP_RATED;
        } else {
            category = ApiUtils.API_CATEGORY_POPULAR;
        }

        Call<MoviesContainer> movieCall = tmdbService.getMoviesByCategory(
                category,
                ApiUtils.API_KEY,
                HomeActivity.deviceLanguage
        );

        movieCall.enqueue(new Callback<MoviesContainer>() {
            @Override
            public void onResponse(@NonNull Call<MoviesContainer> call, @NonNull Response<MoviesContainer> response) {
                // Populate our recycler if the request was successful
                MoviesContainer body = response.body();
                if (body != null && body.getResults() != null) {
                    movieList = body.getResults();
                }
                refreshLayout.setRefreshing(false);
                displayList();
            }

            @Override
            public void onFailure(@NonNull Call<MoviesContainer> call, @NonNull Throwable t) {
                // Display an error on failure
                progressBar.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                if (MiscUtils.isNetworkAvailable(activity)) {
                    emptyText.setText(R.string.no_data_fetched);
                } else if (movieList != null && !movieList.isEmpty()) {
                    Toast.makeText(activity, noConnectionString, Toast.LENGTH_SHORT).show();
                } else {
                    emptyText.setText(noConnectionString);
                }
            }
        });
    }

    /**
     * Displays the list {@link RecyclerView} or informs the user of any errors.
     */
    private void displayList() {
        progressBar.setVisibility(View.GONE);

        // Notify when we lost connection while loading
        if (!MiscUtils.isNetworkAvailable(activity) && movieList == null) {
            emptyText.setText(noConnectionString);
            return;
        } else if (movieList == null) {
            emptyText.setText(R.string.no_data_fetched);
        } else if (movieList.isEmpty()) {
            emptyText.setText(R.string.no_results_found);
            return;
        } else {
            emptyText.setText("");
        }

        MovieRecyclerAdapter resultsRecyclerAdapter = new MovieRecyclerAdapter(activity, PreferenceManager.getDefaultSharedPreferences(activity),movieList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.results_list_grid_columns)));
        recyclerView.setAdapter(resultsRecyclerAdapter);
    }

    /*____________________________________________________________________________________________*/
}