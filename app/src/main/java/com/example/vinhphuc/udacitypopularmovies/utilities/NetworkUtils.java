package com.example.vinhphuc.udacitypopularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by VINH PHUC on 10/3/2018.
 */

public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie";

    //TODO (1) input your API key here
    public static String MOVIEDB_API_KEY = "";

    static final String SORT_PARAM = "sort_by";
    static final String API_KEY_PARAM = "api_key";

    public static URL buildUrl(String[] params) {
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendQueryParameter(SORT_PARAM, params[0])
                .appendQueryParameter(API_KEY_PARAM, MOVIEDB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        InputStream inputStream = urlConnection.getInputStream();
        StringBuilder builder = new StringBuilder();

        if (inputStream == null) {
            disconnectNetwork(urlConnection, reader);
            return null;
        }
        reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }

        if (builder.length() == 0) {
            disconnectNetwork(urlConnection, reader);
            return null;
        }
        return builder.toString();
    }

    public static void disconnectNetwork(HttpURLConnection urlConnection, BufferedReader reader) {
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (final IOException e) {
                Log.e(TAG, "Error closing stream", e);
            }
        }
    }
}
