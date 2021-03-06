/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.movie20.Utils;

import android.os.AsyncTask;

import com.amannawabi.movie20.Model.Trailer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class TrailerThread extends AsyncTask<URL, Void, List<Trailer>> {

    //    public class MovieThread extends AsyncTask<URL, Void, List<Movies>> {
    private final onTrailerTaskCompleted mTaskFinished;
    private static final String TAG = "TrailerThread";

    public TrailerThread(onTrailerTaskCompleted onTrailerTaskCompleted) {

        mTaskFinished = onTrailerTaskCompleted;
    }

    /**
     * Get the URL and starts the getting data from server in background thread and send
     * the data to JSON parser for parsing and stores it in Array List to be sent to UI thread
     */
    @Override
    protected List<Trailer> doInBackground(URL... urls) {
        URL url = urls[0];

        String jSonData;

        List<Trailer> mTrailerList = new ArrayList<>();


        try {
            jSonData = NetworkUtils.getResponseFromHttpUrl(url);
//                Log.d(TAG, "doInBackground: " +jSonData);

            mTrailerList = JsonUtils.parseTrailerJson(jSonData);

//            Log.d(TAG, "doInBackground: " + mTrailerList.get(0).getsTrailerKey());
//            Log.d(TAG, "doInBackground: " + mTrailerList.get(1).getsTrailerKey());
//            Log.d(TAG, "doInBackground: " + mTrailerList.get(2).getsTrailerKey());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mTrailerList;
    }

    /**
     * Gets the background thread result and sends it to UI thread, it also initiate the Recycler Adapter
     * and set the data to Recycler view
     */
    @Override
    protected void onPostExecute(List<Trailer> mTrailerList) {
        super.onPostExecute(mTrailerList);
        mTaskFinished.onTrailerTaskCompleted(mTrailerList);

    }


}
