package com.example.vinhphuc.udacitypopularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinhphuc.udacitypopularmovies.models.Movie.Genre;
import com.example.vinhphuc.udacitypopularmovies.models.Movie.Movie;
import com.example.vinhphuc.udacitypopularmovies.models.Movie.Video;
import com.example.vinhphuc.udacitypopularmovies.models.Movies;
import com.example.vinhphuc.udacitypopularmovies.utilities.ImageUtils;
import com.nex3z.flowlayout.FlowLayout;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by VINH PHUC on 25/3/2018.
 */

public class MovieDetailFragment extends Fragment {
    private final String TAG = MovieDetailFragment.class.getSimpleName();

    public static String EXTRA_MOVIE_KEY = "extra_movie";

    @InjectView(R.id.svDetailsContainer)
    private NestedScrollView mSvDetailsContainer;
    @InjectView(R.id.imageview_poster)
    private ImageView mIvMovie;
    @InjectView(R.id.tvTitle)
    private TextView mTvTitle;
    @InjectView(R.id.tvReleaseDateValue)
    private TextView mTvReleaseDateValue;
    @InjectView(R.id.tvDurationValue)
    private TextView mTvDurationValue;
    @InjectView(R.id.tvVoteValue)
    private TextView mTvVoteAvgValue;
    @InjectView(R.id.tvPlotValue)
    private TextView mTvPlotValue;
    @InjectView(R.id.ratingBar)
    private MaterialRatingBar mRatingBar;
    @InjectView(R.id.genresContainer)
    private FlowLayout mGenresContainer;
    @InjectView(R.id.rvVideos)
    private RecyclerView mRvVideos;
    @InjectView(R.id.tvReviewsTitle)
    private TextView mTvReviewsTitle;
    @InjectView(R.id.rvReviews)
    private RecyclerView mRvReviews;
    @InjectView(R.id.fbLike)
    private FloatingActionButton mFbLike;
    @InjectView(R.id.toolbar_layout)
    private CollapsingToolbarLayout appBarLayout;
    @InjectView(R.id.backDropImage)
    private ImageView backdropImageView;


    private Movie mMovie;

    private boolean mIsFavourite;

    public MovieDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(EXTRA_MOVIE_KEY)) {
            mMovie = getArguments().getParcelable(EXTRA_MOVIE_KEY);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share_action) {
            shareTrailer(mMovie.getVideos().getResults().get(0));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.activity_detail_fragment,
                        container,
                        false);
        ButterKnife.inject(this, rootView);

        mIsFavourite = isFavouriteMovie();

        if (mMovie != null) {
            initializeViews(rootView);
            populateUI();
        } else {
            closeOnError();
        }
        return rootView;
    }

    private void closeOnError() {
        Toast.makeText(getActivity().getApplicationContext(),
                R.string.movie_detail_error_message,
                Toast.LENGTH_SHORT).show();

        // Not in TwoPane Mode so close the MovieDetailActivity
        if (!areOnTwoPaneMove()) {
            getActivity().finish();
        } else {
            mSvDetailsContainer.setVisibility(View.GONE);
        }
    }

    private boolean areOnTwoPaneMove() {
        return (getActivity() instanceof MainActivity);
    }

    private void shareTrailer(Video video) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(
                android.content.Intent.EXTRA_SUBJECT,
                mMovie.getTitle() + " - " + video.getName());
        sharingIntent.putExtra(
                android.content.Intent.EXTRA_TEXT,
                "http://www.youtube.com/watch?v=" + video.getKey());
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_trailer)));
    }

    private void initializeViews(View rootView) {
        ButterKnife.inject(this, rootView);

        mFbLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFavouriteStatus();
            }
        });
    }

    private void switchFavouriteStatus() {
        if (mIsFavourite) {
            Uri uri = MoviesContract.MoviesEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(String.valueOf(mMovie.getId())).build();
            int returnUri = getContext()
                    .getContentResolver()
                    .delete(uri, null, null);
            Log.d(TAG, "ReturnUri: " + returnUri);
            getContext().getContentResolver().notifyChange(uri, null);

            mIsFavourite = !mIsFavourite;
            switchFabStyle();
            Toast.makeText(getActivity().getApplicationContext(),
                    mMovie.getTitle() + " " + getString(R.string.removed_from_favourite),
                    Toast.LENGTH_SHORT).show();
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_ID, mMovie.getId());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE, mMovie.getTitle());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, mMovie.getOverview());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, mMovie.getPosterPath());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH, mMovie.getBackdropPath());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_RUNTIME, mMovie.getRuntime());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());

            Uri uri = getContext()
                    .getContentResolver()
                    .insert(MoviesContract.MoviesEntry.CONTENT_URI, contentValues);
            if (uri != null) {
                mIsFavourite = !mIsFavourite;
                switchFabStyle();
                Toast.makeText(getActivity().getApplicationContext(),
                        mMovie.getTitle() + " " + getString(R.string.added_to_favourite),
                        Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "Uri null");
            }
        }
    }

    private void switchFabStyle() {
        if (mIsFavourite) {
            mFbLike.setImageDrawable(ContextCompat
                            .getDrawable(getContext(), R.drawable.ic_broken_heart));
            mFbLike.setBackgroundTintList(ColorStateList
                    .valueOf(ContextCompat.getColor(getContext(), R.color.colorDarkGrey)));
        } else {
            mFbLike.setImageDrawable(ContextCompat
                    .getDrawable(getContext(), R.drawable.ic_heart));
            mFbLike.setBackgroundTintList(ColorStateList
                    .valueOf(ContextCompat.getColor(getContext(), R.color.colorAccent)));
        }
    }

    private void populateUI() {
        if (appBarLayout != null) {
            appBarLayout.setTitle(mMovie.getTitle());
        }

        switchFabStyle();

        if (backdropImageView != null) {
            backdropImageView.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(getActivity().getApplicationContext())
                            .load(ImageUtils.
                                    buildBackdropImageUrl(
                                            mMovie.getBackdropPath(),
                                            backdropImageView.getWidth()
                                    )
                            )
                            .into(backdropImageView);
                }
            });
        }

        mTvTitle.setText(mMovie.getTitle());
        mTvReleaseDateValue.setText(mMovie
                .getReleaseDateLocalized(getActivity().getApplicationContext()));
        mTvDurationValue.setText(mMovie.getDuration());
        mTvVoteAvgValue.setText(String.valueOf(mMovie.getVoteAverage()));
        mRatingBar.setRating((float) (mMovie.getVoteAverage() / 2));
        mTvPlotValue.setText(mMovie.getOverview());

        for (Genre genre : mMovie.getGenres()) {
            TextView textView = new TextView(getActivity());
            textView.setBackground(
                    ContextCompat.getDrawable(getActivity().getApplicationContext(),
                            R.drawable.label_bg));
            textView.setText(genre.getName());
            textView.setTextColor(ContextCompat
                    .getColor(getActivity().getApplicationContext(), R.color.colorLightGrey));
            mGenresContainer.addView(textView);
        }

        mIvMovie.post(new Runnable() {
            @Override
            public void run() {
                Picasso.with(getActivity().getApplicationContext())
                        .load(ImageUtils.
                                buildPosterImageUrl(
                                        mMovie.getPosterPath(),
                                        mIvMovie.getWidth()
                                ))
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(mIvMovie);
            }
        });

        setUpVideosRecyclerView();
        setUpReviewsRecyclerView();
    }

    private void setUpVideosRecyclerView() {
        if (mMovie.getVideos().getResults().size() > 0) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    mRvVideos.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false);
            mRvVideos.setLayoutManager(layoutManager);
            mRvVideos.setHasFixedSize(true);
            mRvVideos.addItemDecoration(new SpacingItemDecoraton(
                    (int) getResources().getDimension(R.dimen.video_list_items_margin),
                    SpacingItemDecoration.HORIZONTAL)
            );

            mRvVideos.setAdapter(new VideoAdapter(mRvVideos.getContext(), mMovie.getVideos()));
            mRvVideos.setVisibility(View.VISIBLE);

            mRvVideos.setVisibility(View.VISIBLE);
            setHasOptionsMenu(true);
        }
    }

    private void setUpReviewsRecyclerView() {
        if (mMovie.getReviews().getResults().size() > 0) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    mRvVideos.getContext(),
                    LinearLayoutManager.VERTICAL,
                    false
            );
            mRvReviews.setLayoutManager(layoutManager);
            mRvReviews.setHasFixedSize(false);
            mRvReviews.addItemDecoration(new SpacingItemDecoration(
                    (int) getResources().getDimension(R.dimen.movie_list_items_margin),
                    SpacingItemDecoration.VERTICAL)
            );

            mRvReviews.setAdapter(new ReviewAdapter(mMovie.getReviews()));

            mTvReviewsTitle.setVisibility(View.VISIBLE);
            mRvReviews.setVisibility(View.VISIBLE);
        }
    }

    public boolean isFavouriteMovie() {
        final Cursor cursor;
        cursor = getContext()
                .getContentResolver()
                .query(MoviesContract.MoviesEntry.CONTENT_URI,
                        null,
                        "movie_id=?",
                        new String[]{
                            String.valueOf(mMovie.getId())
                        },
                        null);
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }
}
