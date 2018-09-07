package com.example.vinhphuc.udacitypopularmovies.models.movie;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by VINH PHUC on 25/3/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie implements Parcelable {
    @JsonProperty("id")
    private int id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("runtime")
    private int runtime;
    @JsonProperty("vote_average")
    private double voteAverage;
    @JsonProperty("videos")
    private VideoResult videos;
    @JsonProperty("reviews")
    private ReviewList reviews;
    @JsonProperty("genres")
    private List<Genre> genres;

    public Movie() {
        this.id = 0;
        this.title = "";
        this.overview = "";
        this.posterPath = "";
        this.backdropPath = "";
        this.releaseDate = "";
        this.runtime = 0;
        this.voteAverage = 0.0;
        this.videos = new VideoResult();
        this.reviews = new ReviewList();
        this.genres = new ArrayList<>();
    }

    public Movie(
            int id,
            String title,
            String overview,
            String posterPath,
            String backdropPath,
            String releaseDate,
            int runtime,
            double voteAverage,
            VideoResult videos,
            ReviewList reviews,
            List<Genre> genres) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.voteAverage = voteAverage;
        this.videos = videos;
        this.reviews = reviews;
        this.genres = genres;
    }

    // Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
        dest.writeString(this.releaseDate);
        dest.writeInt(this.runtime);
        dest.writeDouble(this.voteAverage);
        dest.writeParcelable(this.videos, flags);
        dest.writeParcelable(this.reviews, flags);
        dest.writeTypedList(this.genres);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.releaseDate = in.readString();
        this.runtime = in.readInt();
        this.voteAverage = in.readDouble();
        this.videos = in.readParcelable(VideoResult.class.getClassLoader());
        this.reviews = in.readParcelable(ReviewList.class.getClassLoader());
        this.genres = in.createTypedArrayList(Genre.CREATOR);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    // Getters
    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getOverview() {
        return this.overview;
    }

    public String getPosterPath() {
        return this.posterPath;
    }

    public String getBackdropPath() {
        return this.backdropPath;
    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public String getReleaseDateLocalized(Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
        Date date = null;
        try {
            date = sdf.parse(releaseDate);
        } catch (ParseException e) {
            return releaseDate;
        }
        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(context);
        return dateFormat.format(date);
    }

    public int getRuntime() {
        return this.runtime;
    }

    public double getVoteAverage() {
        return this.voteAverage;
    }

    public VideoResult getVideos() {
        return this.videos;
    }

    public ReviewList getReviews() {
        return this.reviews;
    }

    public List<Genre> getGenres() {
        return this.genres;
    }

    public String getDuration() {
        return String.valueOf(this.runtime) + " min";
    }
}
