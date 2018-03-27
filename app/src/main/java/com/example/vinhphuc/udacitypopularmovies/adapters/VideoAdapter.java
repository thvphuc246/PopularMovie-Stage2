package com.example.vinhphuc.udacitypopularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vinhphuc.udacitypopularmovies.R;
import com.example.vinhphuc.udacitypopularmovies.holders.VideoViewHolder;
import com.example.vinhphuc.udacitypopularmovies.models.movie.Video;
import com.example.vinhphuc.udacitypopularmovies.models.movie.VideoResult;
import com.squareup.picasso.Picasso;

/**
 * Created by VINH PHUC on 26/3/2018.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private Context mContext;
    private VideoResult mVideoResult;

    public VideoAdapter(Context context, VideoResult videoResult) {
        this.mContext = context;
        this.mVideoResult = videoResult;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final Video video = mVideoResult.getResults().get(position);
        Picasso.with(mContext)
                .load(buildThumbnailUrl(video.getKey()))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.mIvVideoThumb);
        holder.mTvVideoTitle.setText(video.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=".concat(video.getKey()))
                );
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideoResult.getResults().size();
    }

    private String buildThumbnailUrl(String videoId) {
        return "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
    }
}
