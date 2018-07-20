/*
 * Copyright (C) 2013 The Android Open Source Project
 */


package com.amannawabi.movie20.Controller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amannawabi.movie20.Model.Trailer;
import com.amannawabi.movie20.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private final List<Trailer> mTrailerList;
    private static final String TAG = "MovieTrailerAdapter";
    private static final String THUMBNAIL_PATH = "https://img.youtube.com/vi/";
    private static final String THUMBNAIL_EXT = "/mqdefault.jpg";
    final private TrailerAdapter.ListItemClickListener mOnClickListener;
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
    public TrailerAdapter(List<Trailer> trailerList, ListItemClickListener mListItemListener) {
        mTrailerList = trailerList;

        mOnClickListener = mListItemListener;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_layout_rv, parent, false);

        return new TrailerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final TrailerViewHolder holder, final int position) {
        int count = position+1;
        String sName = mTrailerList.get(position).getsName() + " " + count;
        holder.mTrailerType.setText(sName);
        Picasso.get().load(THUMBNAIL_PATH + mTrailerList.get(position).getsTrailerKey() + THUMBNAIL_EXT).into(holder.mTrailerThumbnail);
//        Log.d(TAG, "onBindViewHolder: " + THUMBNAIL_PATH + mTrailerList.get(position).getsTrailerKey()+THUMBNAIL_EXT);
    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView mTrailerType;
        final LinearLayout mParentLayout;
        final ImageView mTrailerThumbnail;

        TrailerViewHolder(View itemView) {
            super(itemView);
            mTrailerType = itemView.findViewById(R.id.trailer_Type);
            mParentLayout = itemView.findViewById(R.id.parent_layout);
            mTrailerThumbnail = itemView.findViewById(R.id.movie_trailer);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

        //
    }
}
