/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.moview.Utils;

import android.os.AsyncTask;
import android.util.Log;

import com.amannawabi.moview.Model.Review;
import com.amannawabi.moview.Model.Trailer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReviewThread extends AsyncTask<URL, Void, List<Review>> {

    //    public class MovieThread extends AsyncTask<URL, Void, List<Movies>> {
    private onReviewTaskCompleted mReviewTaskCompleted;
    private static final String TAG = "MovieHttpHandlerTrailer";

    public ReviewThread(onReviewTaskCompleted reviewTaskCompleted) {
        mReviewTaskCompleted = reviewTaskCompleted;
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
    protected List<Review> doInBackground(URL... urls) {
        URL url = urls[0];

        String jSonData;

        List<Review> mReviewList = new ArrayList<>();


        try {
            jSonData = NetworkUtils.getResponseFromHttpUrl(url);
//                Log.d(TAG, "doInBackground: " +jSonData);

            mReviewList = JsonUtils.parseReviewJson(jSonData);

            Log.d(TAG, "doInBackground: " + mReviewList.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mReviewList;
    }

    /**
     * Gets the background thread result and sends it to UI thread, it also initiate the Recycler Adapter
     * and set the data to Recycler view
     */
    @Override
    protected void onPostExecute(List<Review> mTrailerList) {
        super.onPostExecute(mTrailerList);
        mReviewTaskCompleted.onReviewTaskCompleted(mTrailerList);

    }

}

