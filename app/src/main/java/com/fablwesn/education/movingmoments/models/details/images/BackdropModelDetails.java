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

package com.fablwesn.education.movingmoments.models.details.images;

import android.os.Parcel;
import android.os.Parcelable;

import com.fablwesn.education.movingmoments.models.details.MovieDetailsModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Holds a single movies backdrop images inside {@link MovieDetailsModel}.
 */
public class BackdropModelDetails implements Parcelable {

    final static Creator<BackdropModelDetails> CREATOR = new Creator<BackdropModelDetails>() {

        @SuppressWarnings({
                "unchecked"
        })
        public BackdropModelDetails createFromParcel(Parcel in) {
            return new BackdropModelDetails(in);
        }

        public BackdropModelDetails[] newArray(int size) {
            return (new BackdropModelDetails[size]);
        }

    };

    @SerializedName("file_path")
    @Expose
    private String filePath;

    private BackdropModelDetails(Parcel in) {
        this.filePath = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public BackdropModelDetails() {
    }

    public BackdropModelDetails(
            String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "{backdropImg:" +
                " filePath=" + filePath
                + "}";
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(filePath);
    }

    public int describeContents() {
        return 0;
    }

}