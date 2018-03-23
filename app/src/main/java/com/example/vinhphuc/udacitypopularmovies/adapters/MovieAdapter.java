package com.example.vinhphuc.udacitypopularmovies.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.vinhphuc.udacitypopularmovies.Movie;
import com.example.vinhphuc.udacitypopularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by VINH PHUC on 10/3/2018.
 */

public class MovieAdapter extends BaseAdapter {
    private final Context mContext;
    private final Movie[] mMovies;

    public MovieAdapter(Context context, Movie[] movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public int getCount() {
        if (mMovies == null || mMovies.length == 0) {
            return -1;
        }

        return mMovies.length;
    }

    @Override
    public Movie getItem(int position) {
        if (mMovies == null || mMovies.length == 0) {
            return null;
        }

        return mMovies[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext)
                .load(mMovies[position].getPosterPath())
                .resize(mContext.getResources().getInteger(R.integer.tmdb_poster_w185_width),
                        mContext.getResources().getInteger(R.integer.tmdb_poster_w185_height))
                .error(R.drawable.not_found)
                .placeholder(R.drawable.searching)
                .into(imageView);

        return imageView;
    }
}
