package com.example.vinhphuc.udacitypopularmovies;

import com.example.vinhphuc.udacitypopularmovies.models.Movie.Movie;

/**
 * Created by VINH PHUC on 16/3/2018.
 */

public interface OnTaskCompleted {
    void onFetchMovieTaskCompleted(Movie[] movies);
}
