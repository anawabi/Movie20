/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.moview.Utils;

import android.util.Log;

import com.amannawabi.moview.Model.Movies;
import com.amannawabi.moview.Model.Review;
import com.amannawabi.moview.Model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String TAG = "MovieJsonUtils";

    /**
     * Gets the data as String and parse it to JSON, stores it in Array list and return it
     */

    public static List<Movies> parseMovieJson(String json) {

        Movies movies;
        List<Movies> mMoviesList = new ArrayList<>();
        try {
            JSONObject response = new JSONObject(json);
            JSONArray resultArray = response.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject obj = resultArray.getJSONObject(i);
                movies = new Movies(obj.getInt("id"),
                        obj.getString("title"),
                        obj.getString("poster_path"),
                        obj.getString("overview"),
                        obj.getString("vote_average"),
                        obj.getString("release_date"));

                mMoviesList.add(movies);
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return mMoviesList;
    }

    public static List<Trailer> parseTrailerJson(String json) {

        List<Trailer> mTrailerList = new ArrayList<>();
        Trailer mTrailer = null;
        try {
            JSONObject response = new JSONObject(json);
            JSONArray resultArray = response.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject obj = resultArray.getJSONObject(0);
                mTrailer = new Trailer(obj.getString("id"),
                        obj.getString("key"),
                        obj.getString("type"),
                        obj.getString("name"));

                mTrailerList.add(mTrailer);
            }
//            Log.d(TAG, "parseTrailerJson: " + mTrailerList.size());
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return mTrailerList;
    }

    public static List<Review> parseReviewJson(String json) {

        List<Review> mReviewList = new ArrayList<>();
        Review mReview = null;
        try {
            JSONObject response = new JSONObject(json);
            JSONArray resultArray = response.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject obj = resultArray.getJSONObject(0);
                mReview = new Review(obj.getString("author"),
                        obj.getString("content"));

                mReviewList.add(mReview);
            }
//            Log.d(TAG, "parseTrailerJson: " + mReviewList.size());
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return mReviewList;
    }

}
