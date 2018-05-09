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
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fablwesn.education.movingmoments.R;
import com.fablwesn.education.movingmoments.adapters.DbCursorAdapter;
import com.fablwesn.education.movingmoments.database.MoviesContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Displays a list of favorites, if available
 */
public class SectionFavFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Bindings */

    @BindView(R.id.refreshlay_fav)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.gridview_fav)
    GridView gridView;
    @BindView(R.id.progressbar_fav)
    ProgressBar progressBar;
    @BindView(R.id.empty_txt_fav)
    TextView emptyText;

    /* __________________________________________________________________________________________ */

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Globals */

    private final static String KEY_STATE_SCROLL = "grid_scroll_pos";
    private int scrollPosition;
    private Activity activity;
    private Unbinder viewUnbind;
    private DbCursorAdapter dbCursorAdapter;

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Fundamentals */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_favorites, container, false);
        viewUnbind = ButterKnife.bind(this, view);

        activity = getActivity();
        if(savedInstanceState != null){
            scrollPosition = savedInstanceState.getInt(KEY_STATE_SCROLL);
        }

        gridView.setEmptyView(emptyText);
        prepareSwipeRefresh();
        loadList();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
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
        outState.putInt(KEY_STATE_SCROLL, gridView.getFirstVisiblePosition());
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
                loadList();
            }
        });
    }

    /**
     * Starts loading the list.
     */
    private void loadList() {
        dbCursorAdapter = new DbCursorAdapter(activity, null);

        // start loader
        getLoaderManager().initLoader(0, null, this);
    }

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Loader for the DB query */

    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                MoviesContract.MovieEntry.DB_ID,
                MoviesContract.MovieEntry.COLUMN_TMDB_ID,
                MoviesContract.MovieEntry.COLUMN_ADULT,
                MoviesContract.MovieEntry.COLUMN_TITLE,
                MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
                MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH,
                MoviesContract.MovieEntry.COLUMN_OVERVIEW,
                MoviesContract.MovieEntry.COLUMN_RELEASE,
                MoviesContract.MovieEntry.COLUMN_GENRE,
                MoviesContract.MovieEntry.COLUMN_ORIG_TITLE,
                MoviesContract.MovieEntry.COLUMN_ORIG_LANG,
                MoviesContract.MovieEntry.COLUMN_VOTE_COUNT,
                MoviesContract.MovieEntry.COLUMN_VOTE_AVG
        };
        return new CursorLoader(activity, MoviesContract.MovieEntry.CONTENT_URI, projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        dbCursorAdapter.swapCursor(data);
        gridView.setAdapter(dbCursorAdapter);
        refreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);

        gridView.smoothScrollToPosition(scrollPosition);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        dbCursorAdapter.swapCursor(null);
    }

    /*____________________________________________________________________________________________*/
}
