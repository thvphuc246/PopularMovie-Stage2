package com.example.vinhphuc.udacitypopularmovies.models.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VINH PHUC on 25/3/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewList implements Parcelable {
    @JsonProperty("page")
    private int page;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("results")
    private List<Review> results;
    @JsonProperty("total_results")
    private int totalResults;

    public ReviewList() {
        this.page = 0;
        this.totalPages = 0;
        this.results = new ArrayList<>();
        this.totalResults = 0;
    }

    // Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.page);
        dest.writeInt(this.totalPages);
        dest.writeTypedList(this.results);
        dest.writeInt(this.totalResults);
    }

    protected ReviewList(Parcel in) {
        this.page = in.readInt();
        this.totalPages = in.readInt();
        this.results = in.createTypedArrayList(Review.CREATOR);
        this.totalResults = in.readInt();
    }

    public static final Parcelable.Creator<ReviewList> CREATOR = new Parcelable.Creator<ReviewList>() {
        @Override
        public ReviewList createFromParcel(Parcel parcel) {
            return new ReviewList(parcel);
        }

        @Override
        public ReviewList[] newArray(int size) {
            return new ReviewList[size];
        }
    };

    // Getters
    public int getPage() {
        return this.page;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public List<Review> getResults() {
        return this.results;
    }

    public int getTotalResults() {
        return this.totalResults;
    }
}
