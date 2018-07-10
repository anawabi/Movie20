/*
 * Copyright (C) 2013 The Android Open Source Project
 */


package com.amannawabi.moview.Controller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amannawabi.moview.Model.Review;
import com.amannawabi.moview.Model.Trailer;
import com.amannawabi.moview.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> mReviewList;
    private static final String TAG = "MovieReviewAdapter";

    public ReviewAdapter(List<Review> reviewList) {
        mReviewList = reviewList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout, parent, false);

        return new ReviewViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        int count = position+1;
        holder.mReviewAuthor.setText(mReviewList.get(position).getsAuthor() + " " + count);
        holder.mReviewContent.setText(mReviewList.get(position).getsContent());

    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView mReviewAuthor;
        TextView mReviewContent;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            mReviewAuthor = itemView.findViewById(R.id.review_author);
            mReviewContent = itemView.findViewById(R.id.review_content);
        }
    }
}
