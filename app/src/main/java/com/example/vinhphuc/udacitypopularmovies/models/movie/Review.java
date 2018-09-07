package com.example.vinhphuc.udacitypopularmovies.models.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by VINH PHUC on 25/3/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Review implements Parcelable {
    @JsonProperty("author")
    private String author;
    @JsonProperty("id")
    private String id;
    @JsonProperty("content")
    private String content;
    @JsonProperty("url")
    private String url;

    public Review() {
        this.author = "";
        this.id = "";
        this.content = "";
        this.url = "";
    }

    // Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.id);
        dest.writeString(this.content);
        dest.writeString(this.url);
    }

    protected Review(Parcel in) {
        this.author = in.readString();
        this.id = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    // Getters
    public String getAuthor() {
        return this.author;
    }

    public String getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }

    public String getUrl() {
        return this.url;
    }
}
