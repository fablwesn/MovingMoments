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

import com.fablwesn.education.movingmoments.models.details.images.ImagesContainerDetails;
import com.fablwesn.education.movingmoments.models.details.reviews.ReviewsContainerDetails;
import com.fablwesn.education.movingmoments.models.details.videos.VideosContainerDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a single movies details.
 */
public class MovieDetailsModel implements Parcelable {

    final static Creator<MovieDetailsModel> CREATOR = new Creator<MovieDetailsModel>() {

        @SuppressWarnings({
                "unchecked"
        })
        public MovieDetailsModel createFromParcel(Parcel in) {
            return new MovieDetailsModel(in);
        }

        public MovieDetailsModel[] newArray(int size) {
            return (new MovieDetailsModel[size]);
        }

    };

    @SerializedName("runtime")
    @Expose
    private Integer runtime;
    @SerializedName("budget")
    @Expose
    private Integer budget;
    @SerializedName("revenue")
    @Expose
    private Integer revenue;
    @SerializedName("homepage")
    @Expose
    private String homepage;
    @SerializedName("tagline")
    @Expose
    private String tagline;
    @SerializedName("production_countries")
    @Expose
    private List<DetailsProdCountry> productionCountries = new ArrayList<>();
    @SerializedName("images")
    @Expose
    private ImagesContainerDetails images = new ImagesContainerDetails();
    @SerializedName("videos")
    @Expose
    private VideosContainerDetails videos = new VideosContainerDetails();
    @SerializedName("reviews")
    @Expose
    private ReviewsContainerDetails reviews = new ReviewsContainerDetails();

    private MovieDetailsModel(Parcel in) {
        this.runtime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.budget = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.revenue = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.homepage = ((String) in.readValue((String.class.getClassLoader())));
        this.tagline = ((String) in.readValue((String.class.getClassLoader())));
        in.readTypedList(productionCountries , DetailsProdCountry.CREATOR);
        this.images = ((ImagesContainerDetails) in.readValue((ImagesContainerDetails.class.getClassLoader())));
        this.videos = ((VideosContainerDetails) in.readValue((VideosContainerDetails.class.getClassLoader())));
        this.reviews = ((ReviewsContainerDetails) in.readValue((ReviewsContainerDetails.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public MovieDetailsModel() {
    }

    public MovieDetailsModel(
            Integer runtime,
            Integer budget,
            Integer revenue,
            String homepage,
            String tagline,
            List<DetailsProdCountry> productionCountries,
            ImagesContainerDetails images,
            VideosContainerDetails videos,
            ReviewsContainerDetails reviews) {
        this.runtime = runtime;
        this.budget = budget;
        this.revenue = revenue;
        this.homepage = homepage;
        this.tagline = tagline;
        this.productionCountries = productionCountries;
        this.images = images;
        this.videos = videos;
        this.reviews = reviews;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public List<DetailsProdCountry> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(List<DetailsProdCountry> productionCountries) {
        this.productionCountries = productionCountries;
    }

    public ImagesContainerDetails getImages() {
        return images;
    }

    public void setImages(ImagesContainerDetails images) {
        this.images = images;
    }

    public VideosContainerDetails getVideos() {
        return videos;
    }

    public void setVideos(VideosContainerDetails videos) {
        this.videos = videos;
    }

    public ReviewsContainerDetails getReviews() {
        return reviews;
    }

    public void setReviews(ReviewsContainerDetails reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "{Details:" +
                " runtime=" + runtime +
                " budget=" + budget
                + " revenu=" + revenue
                + " homepage=" + homepage
                + " tagline=" + tagline
                + " productionCountries=" + productionCountries
                + " images=" + images
                + " videos=" + videos
                + " reviews=" + reviews
                + "}";
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(runtime);
        dest.writeValue(budget);
        dest.writeValue(revenue);
        dest.writeValue(homepage);
        dest.writeValue(tagline);
        dest.writeValue(images);
        dest.writeTypedList(productionCountries);
        dest.writeValue(videos);
        dest.writeValue(reviews);
    }

    public int describeContents() {
        return 0;
    }

}