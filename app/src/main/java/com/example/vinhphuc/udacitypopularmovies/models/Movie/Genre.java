package com.example.vinhphuc.udacitypopularmovies.models.Movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by VINH PHUC on 25/3/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Genre implements Parcelable {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;

    public Genre() {
        this.id = 0;
        this.name= "";
    }

    //Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    protected Genre(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel parcel) {
            return new Genre(parcel);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

    // Getters
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}