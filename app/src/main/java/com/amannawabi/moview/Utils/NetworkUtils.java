/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.moview.Utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;


import com.amannawabi.moview.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class NetworkUtils {
    private final static String BASE_URL = "http://api.themoviedb.org/3/movie";
    private final static String YOUTUBE_URL = "https://www.youtube.com/watch";
    private final static String PARAM_V = "v";
    private final static String PARAM_API_KEY = "api_key";
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String TAG = "MovieNetworkUtils";
    private static final String VIDEO = "videos";
    private static final String REVIEW = "reviews";

    /**
     * Generates the URL by receiving the string and appending the path and Query parameters and return the URL
     */
    public static URL buildURL(String sortBy) {
        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(PARAM_API_KEY, API_KEY).build();

        URL Url = null;
        try {
            Url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return Url;
    }

    public static URL buildTrailerURL(String mMovieID) {
        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(mMovieID)
                .appendPath(VIDEO)
                .appendQueryParameter(PARAM_API_KEY, API_KEY).build();

        URL Url = null;
        try {
            Url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return Url;
    }

    public static URL buildReviewURL(String mMovieID) {
        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(mMovieID)
                .appendPath(REVIEW)
                .appendQueryParameter(PARAM_API_KEY, API_KEY).build();

        URL Url = null;
        try {
            Url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return Url;
    }

    public static String buildYouTubeURL(String mTrailerKey) {
        Uri buildUri = Uri.parse(YOUTUBE_URL).buildUpon()
                .appendQueryParameter(PARAM_V, mTrailerKey).build();

        return buildUri.toString();
    }

    /**
     * Establishes HTTP connection with server, opens the connection, buffers the data from server
     * and returns as string and close the connection
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            int status = urlConnection.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
//                    Log.d(TAG, "getResponseFromHttpUrl: " + sb);
                    return sb.toString();
            }
            return null;
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}
