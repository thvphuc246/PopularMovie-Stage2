package com.example.vinhphuc.udacitypopularmovies.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vinhphuc.udacitypopularmovies.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by VINH PHUC on 26/3/2018.
 */

public class VideoViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.videoThumbnail)
    public ImageView mIvVideoThumb;
    @InjectView(R.id.tvVideoTitle)
    public TextView mTvVideoTitle;

    public VideoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
