/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.moview.Controller;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.amannawabi.moview.Model.Movies;
import com.amannawabi.moview.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Generates the View Holder and sets the data into view holder and sends to Recycler view.
 * it populates the movie posters by appending the movie poster path to poster URL and uses
 * Picasso library to download and populate images into UI
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.movieViewHolder> {

    private List<Movies> mMovies;
    private static final String POSTER_PATH = "http://image.tmdb.org/t/p/w185//";
    private static final String TAG = "MovieAdapter";

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    /**
     * Gets the list of data to be populated in the Recycler view each time the method is called
     */
    public MovieAdapter(List<Movies> movies, ListItemClickListener listener) {
        mMovies = movies;
        mOnClickListener = listener;
        this.notifyDataSetChanged();
    }

    public MovieAdapter(ListItemClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    /**
     * Overrides the View Holder and inflates the movie_layout which is a separate XML layout file into
     * Main Activity layout
     */
    @NonNull
    @Override
    public movieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_layout, parent, false);
//        Log.d(TAG, "onCreateViewHolder: started");
        return new movieViewHolder(view);
    }

    /**
     * Populates the movie poster in main UI by referencing its position and using Picasso library to
     * download and populate the poster in UI
     */
    @Override
    public void onBindViewHolder(@NonNull movieViewHolder holder, final int position) {

        try {

            Picasso.get().load(POSTER_PATH + mMovies.get(position).getMoviePoster()).into(holder.mPoster);
//            Log.d(TAG, "onBindViewHolder: ");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the count of all items to be shown in the View Holder and the in recycler view, this method
     * is called before the adapter generates the view holder and populates the UI and if the count is
     * zero then no view holder will be generated and nothing will be shown in recycler view
     */
    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount: " + mMovies.size());
        return mMovies.size();
    }
    public void setMoviess(List<Movies> movies){
        mMovies = movies;
        notifyDataSetChanged();
    }
    public class movieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mPoster;

        public movieViewHolder(View view) {
            super(view);
            mPoster = view.findViewById(R.id.movie_poster);
            view.setOnClickListener(this);

        }

        /**
         * Generates onClick event for the items in Recycler view by identifyind their position
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}





