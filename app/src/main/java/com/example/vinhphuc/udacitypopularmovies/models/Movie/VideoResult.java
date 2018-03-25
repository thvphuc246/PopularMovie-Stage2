package com.example.vinhphuc.udacitypopularmovies.models.Movie;

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
public class VideoResult implements Parcelable {
    @JsonProperty("results")
    private List<Video> results;

    public VideoResult() {
        this.results = new ArrayList<>();
    }

    // Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.results);
    }

    protected VideoResult(Parcel in) {
        this.results = in.createTypedArrayList(Video.CREATOR);
    }

    public static final Creator<VideoResult> CREATOR = new Creator<VideoResult>() {
        @Override
        public VideoResult createFromParcel(Parcel parcel) {
            return new VideoResult(parcel);
        }

        @Override
        public VideoResult[] newArray(int size) {
            return new VideoResult[size];
        }
    };

    // Getters

    public List<Video> getResults() {
        return this.results;
    }
}
