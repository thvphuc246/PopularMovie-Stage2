package com.example.vinhphuc.udacitypopularmovies;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.example.vinhphuc.udacitypopularmovies.utilities.DateTimeUtils;

import java.text.ParseException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailActivity extends AppCompatActivity {
    private final String TAG = DetailActivity.class.getSimpleName();

    private final String parcel_movie = "PARCEL_MOVIE";
    private final String overview_null = "The plot of this movie is currently unknown";
    private final String release_date_null = "No release date found";

    /*
    private TextView tvOriginalTitle;
    private ImageView ivMoviePoster;
    private TextView tvOverview;
    private TextView tvVoteAverage;
    private TextView tvReleaseDate;
    */

    @InjectView(R.id.textview_original_title) TextView tvOriginalTitle;
    @InjectView(R.id.imageview_poster) ImageView ivMoviePoster;
    @InjectView(R.id.textview_overview) TextView tvOverview;
    @InjectView(R.id.textview_vote_average) TextView tvVoteAverage;
    @InjectView(R.id.textview_release_date) TextView tvReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /*
        tvOriginalTitle = (TextView) findViewById(R.id.textview_original_title);
        ivMoviePoster = (ImageView) findViewById(R.id.imageview_poster);
        tvOverview = (TextView) findViewById(R.id.textview_overview);
        tvVoteAverage = (TextView) findViewById(R.id.textview_vote_average);
        tvReleaseDate = (TextView) findViewById(R.id.textview_release_date);
        */

        ButterKnife.inject(this);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(parcel_movie);

        tvOriginalTitle.setText(movie.getOriginalTitle());

        Picasso.with(this)
                .load(movie.getPosterPath())
                .resize(getResources().getInteger(R.integer.tmdb_poster_w185_width),
                        getResources().getInteger(R.integer.tmdb_poster_w185_height))
                .error(R.drawable.not_found)
                .placeholder(R.drawable.searching)
                .into(ivMoviePoster);

        String overview = movie.getOverview();
        if (overview == null) {
            tvOverview.setTypeface(null, Typeface.ITALIC);
            overview = overview_null;
        }
        tvOverview.setText(overview);
        tvVoteAverage.setText(movie.getDetailedVoteAverage());

        /*
        * First get the release date from the object - to be used if something goes wrong with
        * getting localized release date (catch).
        * */
        String releaseDate = movie.getReleaseDate();
        if (releaseDate != null) {
            try {
                releaseDate = DateTimeUtils.getLocalizedDate(this, releaseDate, movie.getDateFormat());
            } catch (ParseException e) {
                Log.e(TAG, "Error with parsing movie release date", e);
            }
        } else {
            tvReleaseDate.setTypeface(null, Typeface.ITALIC);
            releaseDate = release_date_null;
        }
        tvReleaseDate.setText(releaseDate);
    }
}
