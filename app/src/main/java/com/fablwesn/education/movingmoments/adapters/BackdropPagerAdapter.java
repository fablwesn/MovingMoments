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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fablwesn.education.movingmoments.R;
import com.fablwesn.education.movingmoments.models.details.images.BackdropModelDetails;
import com.fablwesn.education.movingmoments.utility.ApiUtils;

import java.util.List;

/**
 * Adapter for the sliding backdrop images {@link com.fablwesn.education.movingmoments.DetailsActivity}
 */
public class BackdropPagerAdapter extends PagerAdapter {

    private Context context;
    private List<BackdropModelDetails> backdropPaths;

    public BackdropPagerAdapter(Context context, List<BackdropModelDetails> backdropPaths) {
        this.context = context;
        this.backdropPaths = backdropPaths;
    }

    @Override
    public int getCount() {
        int size = backdropPaths.size();

        if (size <= context.getResources().getInteger(R.integer.details_indicator_max_size))
            return size;
        else
            return context.getResources().getInteger(R.integer.details_indicator_max_size);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.backdrop_pager_details, container, false);

        ImageView backdropImg = view.findViewById(R.id.details_backdrop_img);
        // load backdropImg
        Glide.with(context)
                .load(ApiUtils.BASE_IMAGES_URL + ApiUtils.getBackdropSizePath(context) + backdropPaths.get(position).getFilePath())
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.vector_img_loading)
                        .error(R.drawable.vector_no_image))
                .into(backdropImg);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}