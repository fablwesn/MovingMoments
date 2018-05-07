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
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fablwesn.education.movingmoments.DetailsActivity;
import com.fablwesn.education.movingmoments.DetailsFavsActivity;
import com.fablwesn.education.movingmoments.HomeActivity;
import com.fablwesn.education.movingmoments.R;
import com.fablwesn.education.movingmoments.database.MoviesContract;
import com.fablwesn.education.movingmoments.models.basic.MovieModel;
import com.fablwesn.education.movingmoments.utility.ApiUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.ViewHolder> {

    /* ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Fundamentals
       ¯¯¯¯¯¯¯¯¯¯¯¯
    */

    private final List<MovieModel> movieList;
    private Activity activity;
    private SharedPreferences sharedPreferences;

    public MovieRecyclerAdapter(Activity activity, SharedPreferences sharedPreferences, List<MovieModel> movieList) {
        this.movieList = movieList;
        this.activity = activity;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_movie, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Checking feed size to handle IndexOutOfBoundsException error
        if (position >= getItemCount())
            return;

        final MovieModel movieModel = movieList.get(position);

        // Set the width of the poster image to perfectly fit the screen
        holder.posterImg.getLayoutParams().width = Math.round(HomeActivity.screenWidthPx / activity.getResources().getInteger(R.integer.results_list_grid_columns));

        // load posterImg
        Glide.with(activity)
                .load(ApiUtils.BASE_IMAGES_URL + ApiUtils.getPosterSizePath(activity) + movieModel.getPosterPath())
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.vector_img_loading)
                        .error(R.drawable.vector_no_image))
                .into(holder.posterImg);

        // Open the DetailsActivity on click.
        holder.posterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int movieTmdbId = movieModel.getId();
                // If the movie is saved inside our DB, start the DetailsFavActivity passing the movies uri
                if (sharedPreferences.getBoolean(String.valueOf(movieTmdbId), false)) {
                    Uri selectedUri = ContentUris.withAppendedId(MoviesContract.MovieEntry.CONTENT_URI, movieTmdbId);
                    Intent intent = new Intent(activity, DetailsFavsActivity.class);
                    intent.setData(selectedUri);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                // If not, start the DetailsActivity and save the already loaded data into a model to reuse inside the next activity
                else {
                    MovieModel dataToSend = new MovieModel(
                            movieModel.getPosterPath(),
                            movieModel.getAdult(),
                            movieModel.getOverview(),
                            movieModel.getReleaseDate(),
                            movieModel.getGenreIds(),
                            movieModel.getId(),
                            movieModel.getOriginalTitle(),
                            movieModel.getOriginalLanguage(),
                            movieModel.getTitle(),
                            movieModel.getBackdropPath(),
                            movieModel.getVoteCount(),
                            movieModel.getVoteAverage()
                    );

                    final Intent intent = new Intent(activity, DetailsActivity.class);
                    intent.putExtra(DetailsActivity.KEY_MOVIE_LOADED, dataToSend);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    /* __________________________________________________________________________________________ */

    /* ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       ViewHolder */

    // Single list item, get everything we need to display the results correctly
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_recycler_poster)
        ImageView posterImg;

        ViewHolder(View listItem) {
            super(listItem);
            ButterKnife.bind(this, listItem);
        }
    }

    /* __________________________________________________________________________________________ */
}