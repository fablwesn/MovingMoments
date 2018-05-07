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

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fablwesn.education.movingmoments.R;
import com.fablwesn.education.movingmoments.models.details.videos.VideoModelDetails;
import com.fablwesn.education.movingmoments.utility.ApiUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for the Recycler view, displays the media results as a list and styles the views.
 */
public class VideosRecyclerAdapter extends RecyclerView.Adapter<VideosRecyclerAdapter.MyViewHolder> {
    /* ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Fundamentals
       ¯¯¯¯¯¯¯¯¯¯¯¯
    */

    private Context context;
    private final List<VideoModelDetails> mediaModelList;

    public VideosRecyclerAdapter(List<VideoModelDetails> list) {
        mediaModelList = list;
    }

    @NonNull
    @Override
    public VideosRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_videos, parent, false);

        context = itemView.getContext();

        return new VideosRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosRecyclerAdapter.MyViewHolder holder, int position) {
        // Checking feed size to handle IndexOutOfBoundsException error
        if (position >= getItemCount())
            return;

        // Get current video.
        final VideoModelDetails mediaModel = mediaModelList.get(position);

        // Load preview thumbnail
        Glide.with(context)
                .load(ApiUtils.YOUTUBE_THUMBNAIL_START + mediaModel.getKey() + ApiUtils.YOUTUBE_THUMBNAIL_END)
                .apply(new RequestOptions()
                        .centerCrop()
                )
                .into(holder.previewImg);

        // Update text
        holder.mediaTitle.setText(mediaModel.getName());
        holder.mediaType.setText(mediaModel.getType());

        // Open video in youtube app or web if no app available.
        holder.previewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApiUtils.YOUTUBE_APP_INTENT_URI + mediaModel.getKey()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(ApiUtils.YOUTUBE_WEB_INTENT_URI + mediaModel.getKey()));
                try {
                    context.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(webIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaModelList.size();
    }

    /* __________________________________________________________________________________________ */

    /* ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       ViewHolder
       ¯¯¯¯¯¯¯¯¯¯
    */
    // single list item, get everything we need to display the results correctly
    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_video_name_txt)
        TextView mediaTitle;
        @BindView(R.id.item_video_type_txt)
        TextView mediaType;
        @BindView(R.id.item_video_thumbnail_img)
        ImageView previewImg;

        MyViewHolder(View listItem) {
            super(listItem);
            ButterKnife.bind(this, listItem);
        }
    }

    /* __________________________________________________________________________________________ */

}
