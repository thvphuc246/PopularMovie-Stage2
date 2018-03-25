package com.example.vinhphuc.udacitypopularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VINH PHUC on 10/3/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movies implements Parcelable {
    @JsonProperty("page")
    private Integer page;
    @JsonProperty("results")
    private List<MovieResult> resultList;

    public Movies() {
        this.page = 0;
        this.resultList = new ArrayList<>();
    }

    //Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.page);
        dest.writeTypedList(this.resultList);
    }

    protected Movies(Parcel in) {
        this.page = (Integer) in.readValue(Integer.class.getClassLoader());
        this.resultList = in.createTypedArrayList(MovieResult.CREATOR);
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel parcel) {
            return new Movies(parcel);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    //Getters & Setters
    public Integer getPage() {
        return this.page;
    }

    public List<MovieResult> getResultList() {
        return this.resultList;
    }

    public void appendMovies(Movies movies) {
        this.page = movies.getPage();
        this.resultList.addAll(movies.getResultList());
    }
}
