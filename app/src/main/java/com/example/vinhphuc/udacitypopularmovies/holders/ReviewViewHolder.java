package com.example.vinhphuc.udacitypopularmovies.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.vinhphuc.udacitypopularmovies.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by VINH PHUC on 26/3/2018.
 */

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.tvReviewAuthor)
    public TextView mTvReviewAuthor;
    @InjectView(R.id.tvReviewContent)
    public TextView mTvReviewContent;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
