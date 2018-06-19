package com.amannawabi.moview.Utils;


import android.os.AsyncTask;
import android.util.Log;

import com.amannawabi.moview.MainActivity;
import com.amannawabi.moview.Model.Movies;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Enable the Application to retrieve movie data from MovieDB in a background thread and send the data
 * to UI tread through Async Task methods
 */

public class FavoriteThread extends AsyncTask<Void, Void, List<Movies>> {
    private onFavoriteTaskCompleted mFavoriteTaskCompleted;
    private static final String TAG = "FavoriteThreadHandler";

    public FavoriteThread(onFavoriteTaskCompleted TaskCompleted) {

        mFavoriteTaskCompleted = TaskCompleted;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Get the URL and starts the getting data from server in background thread and send
     * the data to JSON parser for parsing and stores it in Array List to be sent to UI thread
     */
    @Override
    protected List<Movies> doInBackground(Void... voids) {
        String jSonData;

        List<Movies> mMovielist = new ArrayList<>();

//        try {
        mMovielist = MainActivity.mMovie20DB.mMovieDAO().viewMovie();
        Log.d(TAG, "doInBackground: " +mMovielist.size());

//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
