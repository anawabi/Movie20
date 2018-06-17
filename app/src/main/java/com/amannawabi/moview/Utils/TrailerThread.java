package com.amannawabi.moview.Utils;

import android.os.AsyncTask;
import android.util.Log;

import com.amannawabi.moview.Model.Trailer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class TrailerThread extends AsyncTask<URL, Void, List<Trailer>> {

    //    public class MovieThread extends AsyncTask<URL, Void, List<Movies>> {
    private onTrailerTaskCompleted mTaskFinishwd;
    private static final String TAG = "MovieHttpHandlerTrailer";

    public TrailerThread(onTrailerTaskCompleted onTrailerTaskCompleted) {

        mTaskFinishwd = onTrailerTaskCompleted;
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
    protected List<Trailer> doInBackground(URL... urls) {
        URL url = urls[0];

        String jSonData;

        List<Trailer> mTrailerList = new ArrayList<>();


        try {
            jSonData = NetworkUtils.getResponseFromHttpUrl(url);
//                Log.d(TAG, "doInBackground: " +jSonData);

            mTrailerList = JsonUtils.parseTrailerJson(jSonData);

//            Log.d(TAG, "doInBackground: " + mTrailerList.size());

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
        mTaskFinishwd.onTrailerTaskCompleted(mTrailerList);

    }


}
