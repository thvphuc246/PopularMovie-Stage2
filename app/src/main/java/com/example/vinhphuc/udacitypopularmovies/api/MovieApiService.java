package com.example.vinhphuc.udacitypopularmovies.api;

/**
 * Created by VINH PHUC on 24/3/2018.
 */

import com.example.vinhphuc.udacitypopularmovies.models.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface MovieApiService {
    @GET("movie/top_rated")
    Call<Movies> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/popular")
    Call<Movies> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{movieId}?append_to_response=videos,reviews")
    Call<Movies> getMovie(@Path("movieId") int movieId, @Query("api_key") String apiKey);
}
