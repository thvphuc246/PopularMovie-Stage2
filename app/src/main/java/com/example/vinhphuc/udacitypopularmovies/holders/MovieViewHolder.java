package com.example.vinhphuc.udacitypopularmovies.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.vinhphuc.udacitypopularmovies.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by VINH PHUC on 24/3/2018.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder {
    public ImageView mIvMovie;

    @InjectView(R.id.progressBarContainer)
    LinearLayout mProgressBarContainer;

    public MovieViewHolder(View itemView) {
        super(itemView);

        ButterKnife.inject(this, itemView);
        mIvMovie = itemView.findViewById(R.id.cvVideo);
    }

    public void showProgress(Boolean show) {
        mProgressBarContainer.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
