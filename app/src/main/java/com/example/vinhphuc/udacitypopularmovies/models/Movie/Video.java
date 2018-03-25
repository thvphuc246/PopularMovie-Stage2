package com.example.vinhphuc.udacitypopularmovies.models.Movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by VINH PHUC on 25/3/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Video implements Parcelable {
    @JsonProperty("site")
    private String site;
    @JsonProperty("size")
    private int size;
    @JsonProperty("iso_3166_1")
    private String iso31661;
    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("iso_639_1")
    private String iso6391;
    @JsonProperty("key")
    private String key;

    public Video() {
        this.site = "";
        this.size = 0;
        this.iso31661 = "";
        this.name = "";
        this.id = "";
        this.type = "";
        this.iso6391 = "";
        this.key = "";
    }

    // Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.site);
        dest.writeInt(this.size);
        dest.writeString(this.iso31661);
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeString(this.iso6391);
        dest.writeString(this.key);
    }

    protected Video(Parcel in) {
        this.site = in.readString();
        this.size = in.readInt();
        this.iso31661 = in.readString();
        this.name = in.readString();
        this.id = in.readString();
        this.type = in.readString();
        this.iso6391 = in.readString();
        this.key = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel parcel) {
            return new Video(parcel);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    // Getters
    public String getSite() {
        return this.site;
    }

    public int getSize() {
        return this.size;
    }

    public String getIso31661() {
        return this.iso31661;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getIso6391() {
        return this.iso6391;
    }

    public String getKey() {
        return this.key;
    }
}
