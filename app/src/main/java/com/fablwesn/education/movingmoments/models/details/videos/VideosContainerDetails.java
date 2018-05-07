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

package com.fablwesn.education.movingmoments.models.details.videos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a list of queried videos {@link VideoModelDetails}.
 */
public class VideosContainerDetails implements Parcelable {

    public final static Creator<VideosContainerDetails> CREATOR = new Creator<VideosContainerDetails>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VideosContainerDetails createFromParcel(Parcel in) {
            return new VideosContainerDetails(in);
        }

        public VideosContainerDetails[] newArray(int size) {
            return (new VideosContainerDetails[size]);
        }

    };

    @SerializedName("results")
    private List<VideoModelDetails> results = new ArrayList<>();

   private VideosContainerDetails(Parcel in) {
        in.readTypedList(results, VideoModelDetails.CREATOR);
    }

    public VideosContainerDetails() {
    }

    public List<VideoModelDetails> getResults() {
        return results;
    }

    public void setResults(List<VideoModelDetails> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "{results:" + results +
                "}";
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(results);
    }

    public int describeContents() {
        return 0;
    }

}