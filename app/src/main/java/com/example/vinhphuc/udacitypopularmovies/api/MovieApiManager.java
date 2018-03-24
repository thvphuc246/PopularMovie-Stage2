package com.example.vinhphuc.udacitypopularmovies.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.vinhphuc.udacitypopularmovies.Movie;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by VINH PHUC on 24/3/2018.
 */

public final class MovieApiManager implements Serializable {
    private static final String TAG = MovieApiManager.class.getSimpleName();
    private static volatile MovieApiManager sharedInstance = new MovieApiManager();

    public static final String MOVIEDB_API_KEY = "663b2941e9827e2d9c06ea5a9535f04d";
    public static final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/";
    public static final String MOVIEDB_IMAGE_URL = "http://image.tmdb.org/t/p/";

    static final String SORT_PARAM = "sort_by";
    static final String API_KEY_PARAM = "api_key";

    private MovieApiService movieApiService;

    private MovieApiManager() {
        //Prevent from the reflection api.
        if (sharedInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIEDB_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        movieApiService = retrofit.create(MovieApiService.class);
    }

    public static MovieApiManager getInstance() {
        if (sharedInstance == null) {
            synchronized (MovieApiManager.class) {
                if (sharedInstance == null) sharedInstance = new MovieApiManager();
            }
        }

        return sharedInstance;
    }

    public enum SortBy {
        MostPopular(0),
        TopRated(1),
        Favourite(2);

        private int mValue;

        SortBy(int value) {
            this.mValue = value;
        } // Constructor

        public int id() {
            return mValue;
        }                  // Return enum index

        public static SortBy fromId(int value) {
            for (SortBy color : values()) {
                if (color.mValue == value) {
                    return color;
                }
            }
            return MostPopular;
        }
    }

    public Call<Movie> getMovie(int movieId, final MovieApiCallback<Movie> movieApiCallback) {
        Call<Movie> call = movieApiService.getMovie(movieId, MOVIEDB_API_KEY);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                movieApiCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    Log.e(TAG, "Request was cancelled");
                    movieApiCallback.onCancel();
                } else {
                    Log.e(TAG, t.getMessage());
                    movieApiCallback.onResponse(null);
                }
            }
        });

        return call;
    }

    public void getMovies(SortBy sortBy, int page, MovieApiCallback<Movie> movieApiCallback) {

        switch (sortBy) {
            case MostPopular:
                getPopularMovies(page, movieApiCallback);
                break;
            case TopRated:
                getTopRatedMovies(page, movieApiCallback);
                break;
        }

    }

    private void getPopularMovies(int page, final MovieApiCallback<Movie> movieApiCallback) {
        movieApiService.getPopularMovies(MOVIEDB_API_KEY, page).enqueue(new Callback<Movie>() {

            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                movieApiCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    Log.e(TAG, "Request was cancelled");
                    movieApiCallback.onCancel();
                } else {
                    Log.e(TAG, t.getMessage());
                    movieApiCallback.onResponse(null);
                }
            }

        });
    }

    private void getTopRatedMovies(int page, final MovieApiCallback<Movie> movieApiCallback) {
        movieApiService.getTopRatedMovies(MOVIEDB_API_KEY, page).enqueue(new Callback<Movie>() {

            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                movieApiCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    Log.e(TAG, "Request was cancelled");
                    movieApiCallback.onCancel();
                } else {
                    Log.e(TAG, t.getMessage());
                    movieApiCallback.onResponse(null);
                }
            }

        });
    }
}
