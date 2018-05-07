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

package com.fablwesn.education.movingmoments.models.details;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Holds a single movies production countries inside {@link MovieDetailsModel}.
 */
public class DetailsProdCountry implements Parcelable {

    final static Creator<DetailsProdCountry> CREATOR = new Creator<DetailsProdCountry>() {

        @SuppressWarnings({
                "unchecked"
        })
        public DetailsProdCountry createFromParcel(Parcel in) {
            return new DetailsProdCountry(in);
        }

        public DetailsProdCountry[] newArray(int size) {
            return (new DetailsProdCountry[size]);
        }

    };

    @SerializedName("iso_3166_1")
    @Expose
    private String countryIso;
    @SerializedName("name")
    @Expose
    private String countryName;

    private DetailsProdCountry(Parcel in) {
        this.countryIso = ((String) in.readValue((String.class.getClassLoader())));
        this.countryName = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public DetailsProdCountry() {
    }

    public DetailsProdCountry(
            String countryIso,
            String countryName) {
        this.countryIso = countryIso;
        this.countryName = countryName;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String toString() {
        return "{Videos:" +
                " countryIso=" + countryIso +
                " countryName=" + countryName
                + "}";
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(countryIso);
        dest.writeValue(countryName);
    }

    public int describeContents() {
        return 0;
    }

}