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

package com.fablwesn.education.movingmoments.adapters;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fablwesn.education.movingmoments.DetailsFavsActivity;
import com.fablwesn.education.movingmoments.HomeActivity;
import com.fablwesn.education.movingmoments.R;
import com.fablwesn.education.movingmoments.database.MoviesContract;
import com.fablwesn.education.movingmoments.database.MoviesContract.MovieEntry;
import com.fablwesn.education.movingmoments.utility.ApiUtils;

/**
 * Adapter used to fill the list inside the {@link com.fablwesn.education.movingmoments.HomeActivity}
 * displaying the favorites.
 * Using data from the database {@link com.fablwesn.education.movingmoments.database.MoviesContract}
 */
public class DbCursorAdapter extends CursorAdapter {

    private Activity activity;

    /**
     * Constructor
     *
     * @param activity using the adapter
     * @param cursor   to add the adapter to
     */
    public DbCursorAdapter(Activity activity, Cursor cursor) {
        super(activity, cursor, 0);
        this.activity = activity;
    }

    /**
     * Instantiate a empty view to use
     *
     * @param context of the activity
     * @param cursor  used
     * @param parent  where to attach the layout to
     * @return empty list item view
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_movie, parent, false);
    }

    /**
     * Add data to the list view
     *
     * @param view    list item selected
     * @param context of the activity
     * @param cursor  used
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // get the data we want to attach
        final String poster_path = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));
        final int tmdbId = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_TMDB_ID));

        // get the views that will be holding the data
        final ImageView posterImg = view.findViewById(R.id.img_recycler_poster);
        posterImg.getLayoutParams().width = Math.round(HomeActivity.screenWidthPx / context.getResources().getInteger(R.integer.results_list_grid_columns));

        // load the poster image
        Glide.with(context)
                .load(ApiUtils.BASE_IMAGES_URL + ApiUtils.getPosterSizePath(context) + poster_path)
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.vector_img_loading)
                        .error(R.drawable.vector_no_image))
                .into(posterImg);

        // Save the uri of the movie so the next activity knows which movie to display
        posterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri selectedUri = ContentUris.withAppendedId(MoviesContract.MovieEntry.CONTENT_URI, tmdbId);
                Intent intent = new Intent(activity, DetailsFavsActivity.class);
                intent.setData(selectedUri);
                activity.startActivity(intent);
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
}