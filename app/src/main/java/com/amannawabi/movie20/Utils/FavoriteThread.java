/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.movie20.Utils;



import android.os.AsyncTask;
import android.util.Log;

import com.amannawabi.movie20.Data.Movie20Database;
import com.amannawabi.movie20.MainActivity;
import com.amannawabi.movie20.Model.Movies;

import java.util.List;

/**
 * Enable the Application to retrieve movie data from MovieDB in a background thread and send the data
 * to UI tread through Async Task methods
 */

public class FavoriteThread extends AsyncTask<Void, Void, List<Movies>> {
    private final onFavoriteTaskCompleted mFavoriteTaskCompleted;
    private static final String TAG = "FavoriteThreadHandler";


    public FavoriteThread(onFavoriteTaskCompleted TaskCompleted) {

        mFavoriteTaskCompleted = TaskCompleted;
    }


    /**
     * Get the URL and starts the getting data from server in background thread and send
     * the data to JSON parser for parsing and stores it in Array List to be sent to UI thread
     */
    @Override
    protected List<Movies> doInBackground(Void... voids) {
        Movie20Database mMovieDB = MainActivity.mMovie20DB;
        List<Movies> mMovielist = mMovieDB.mMovieDAO().viewMovie();
//        MainActivity.mMovie20DB.mMovieDAO().viewMovie();
        Log.d(TAG, "doInBackground: " +mMovielist.size());

        return mMovielist;
    }

    /**
     * Gets the background thread result and sends it to UI thread, it also initiate the Recycler Adapter
     * and set the data to Recycler view
     */
    @Override
    protected void onPostExecute(List<Movies> movies) {
        super.onPostExecute(movies);
        mFavoriteTaskCompleted.onFavoriteTaskCompleted(movies);


    }
}
