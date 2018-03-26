package com.example.vinhphuc.udacitypopularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vinhphuc.udacitypopularmovies.R;
import com.example.vinhphuc.udacitypopularmovies.holders.ReviewViewHolder;
import com.example.vinhphuc.udacitypopularmovies.models.Movie.Review;
import com.example.vinhphuc.udacitypopularmovies.models.Movie.ReviewList;

/**
 * Created by VINH PHUC on 26/3/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    private ReviewList mReviews;

    public ReviewAdapter(ReviewList reviews) {
        this.mReviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        final Review review = mReviews.getResults().get(position);
        holder.mTvReviewAuthor.setText(review.getAuthor());
        holder.mTvReviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.getResults().size();
    }
}
