package com.example.vinhphuc.udacitypopularmovies;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.vinhphuc.udacitypopularmovies.models.Movie.Movie;
import com.example.vinhphuc.udacitypopularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by VINH PHUC on 10/3/2018.
 */

public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {
    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

//    private final String mApiKey;
    private final OnTaskCompleted mListener;

    public FetchMovieTask(OnTaskCompleted mListener) {
        super();
        this.mListener = mListener;
    }

    @Override
    protected Movie[] doInBackground(String... params) {
        String moviesJsonStr = null;
        try {
            URL url = NetworkUtils.buildUrl(params);
            moviesJsonStr = NetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);
        }

        try {
            return getMoviesDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    private Movie[] getMoviesDataFromJson(String moviesJsonStr) throws JSONException {
        // JSON tags
        final String TAG_RESULTS = "results";
        final String TAG_ORIGINAL_TITLE = "original_title";
        final String TAG_POSTER_PATH = "poster_path";
        final String TAG_OVERVIEW = "overview";
        final String TAG_VOTE_AVERAGE = "vote_average";
        final String TAG_RELEASE_DATE = "release_date";

        // Get the array containing hte movies found
        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = moviesJson.getJSONArray(TAG_RESULTS);

        // Create array of Movie objects that stores data from the JSON string
        Movie[] movies = new Movie[resultsArray.length()];

        // Traverse through movies one by one and get data
        for (int i = 0; i < resultsArray.length(); i++) {
            // Initialize each object before it can be used
            movies[i] = new Movie();

            // Object contains all tags we're looking for
            JSONObject movieInfo = resultsArray.getJSONObject(i);

            // Store data in movie object
            movies[i].setOriginalTitle(movieInfo.getString(TAG_ORIGINAL_TITLE));
            movies[i].setPosterPath(movieInfo.getString(TAG_POSTER_PATH));
            movies[i].setOverview(movieInfo.getString(TAG_OVERVIEW));
            movies[i].setVoteAverage(movieInfo.getDouble(TAG_VOTE_AVERAGE));
            movies[i].setReleaseDate(movieInfo.getString(TAG_RELEASE_DATE));
        }

        return movies;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);
        //Notify UI
        mListener.onFetchMovieTaskCompleted(movies);
    }
}
