package com.example.vinhphuc.udacitypopularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vinhphuc.udacitypopularmovies.DetailActivity;
import com.example.vinhphuc.udacitypopularmovies.MainActivity;
import com.example.vinhphuc.udacitypopularmovies.MovieDetailFragment;
import com.example.vinhphuc.udacitypopularmovies.models.movie.Movie;
import com.example.vinhphuc.udacitypopularmovies.models.MovieList;
import com.example.vinhphuc.udacitypopularmovies.R;
import com.example.vinhphuc.udacitypopularmovies.api.MovieApiCallback;
import com.example.vinhphuc.udacitypopularmovies.api.MovieApiManager;
import com.example.vinhphuc.udacitypopularmovies.holders.MovieViewHolder;
import com.example.vinhphuc.udacitypopularmovies.utilities.ImageUtils;
import com.squareup.picasso.Picasso;

import retrofit2.Call;

/**
 * Created by VINH PHUC on 10/3/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private MainActivity mParentActivity;
    private MovieList mMovies;
    private boolean mTwoPane;
    private Call<Movie> callRequest;

    public MovieAdapter(MainActivity parent, MovieList movies, boolean twoPane) {
        this.mParentActivity = parent;
        this.mMovies = movies;
        this.mTwoPane = twoPane;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        final int pos = position;
        holder.mIvMovie.post(new Runnable() {
            @Override
            public void run() {
                Picasso.with(mParentActivity.getApplicationContext())
                        .load(ImageUtils.buildPosterImageUrl(mMovies.getResultList().get(pos).getPosterPath(), holder.mIvMovie.getWidth()))
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(holder.mIvMovie);
            }
        });

        holder.itemView.setTag(mMovies.getResultList().get(position).getId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callRequest != null) {
                    callRequest.cancel();
                }

                getMovieAndShowDetails((int) view.getTag(), holder);
            }
        });

        if (position == 0 && mTwoPane) {
            holder.itemView.callOnClick();
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.getResultList().size();
    }

    public void updateMovies(MovieList movies) {
        int position = this.mMovies.getResultList().size() + 1;
        this.mMovies.appendMovies(movies);
        notifyItemRangeInserted(position, movies.getResultList().size());
    }

    private void getMovieAndShowDetails(final int movieId, final MovieViewHolder movieViewHolder) {
        final Context context = mParentActivity;

        if (isNetworkAvailable(context)) {

            movieViewHolder.showProgress(true);
            callRequest = MovieApiManager.getInstance().getMovie(movieId, new MovieApiCallback<Movie>() {
                @Override
                public void onResponse(Movie result) {

                    if (result != null) {
                        if (mTwoPane) {
                            Bundle arguments = new Bundle();
                            arguments.putParcelable(MovieDetailFragment.EXTRA_MOVIE_KEY, result);
                            MovieDetailFragment fragment = new MovieDetailFragment();
                            fragment.setArguments(arguments);
                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.movieDetailContainer, fragment)
                                    .commit();
                        } else {
                            Intent intent = new Intent(context, DetailActivity.class);
                            intent.putExtra(MovieDetailFragment.EXTRA_MOVIE_KEY, result);

                            context.startActivity(intent);
                        }
                    } else {
                        Toast.makeText(context, R.string.movie_detail_error_message, Toast.LENGTH_SHORT).show();
                    }

                    callRequest = null;
                    movieViewHolder.showProgress(false);
                }

                @Override
                public void onCancel() {
                    callRequest = null;
                    movieViewHolder.showProgress(false);
                }

            });
        } else {
            Toast.makeText(context, R.string.error_need_internet, Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
