package com.example.vinhphuc.udacitypopularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by VINH PHUC on 25/3/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieResult implements Parcelable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("poster_path")
    private String posterPath;

    public MovieResult() {
        this.id = 0;
        this.posterPath = "";
    }

    //Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.posterPath);
    }

    protected MovieResult(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.posterPath = in.readString();
    }

    public static final Creator<MovieResult> CREATOR = new Creator<MovieResult>() {
        @Override
        public MovieResult createFromParcel(Parcel parcel) {
            return new MovieResult(parcel);
        }

        @Override
        public MovieResult[] newArray(int size) {
            return new MovieResult[size];
        }
    };

    //Getter
    public Integer getId() {
        return this.id;
    }

    public String getPosterPath() {
        return this.posterPath;
    }
}
