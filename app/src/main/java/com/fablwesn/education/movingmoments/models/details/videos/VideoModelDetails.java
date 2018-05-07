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

import com.fablwesn.education.movingmoments.models.details.MovieDetailsModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Holds a single movies videos inside {@link MovieDetailsModel}.
 */
public class VideoModelDetails implements Parcelable {

    final static Creator<VideoModelDetails> CREATOR = new Creator<VideoModelDetails>() {

        @SuppressWarnings({
                "unchecked"
        })
        public VideoModelDetails createFromParcel(Parcel in) {
            return new VideoModelDetails(in);
        }

        public VideoModelDetails[] newArray(int size) {
            return (new VideoModelDetails[size]);
        }

    };

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;

    private VideoModelDetails(Parcel in) {
        this.key = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public VideoModelDetails() {
    }

    public VideoModelDetails(
            String key,
            String name,
            String type) {
        this.key = key;
        this.name = name;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "{Videos:" +
                " key=" + key +
                " name=" + name
                + " type=" + type
                + "}";
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(key);
        dest.writeValue(name);
        dest.writeValue(type);
    }

    public int describeContents() {
        return 0;
    }

}