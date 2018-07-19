/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.movie20.Utils;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Display;

import com.amannawabi.movie20.Data.Movie20Database;
import com.amannawabi.movie20.DetailedLayout;
import com.amannawabi.movie20.MainActivity;
import com.amannawabi.movie20.Model.MovieViewModel;
import com.amannawabi.movie20.Model.Movies;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Enable the Application to retrieve movie data from MovieDB in a background thread and send the data
 * to UI tread through Async Task methods
 */

public class FavoriteThread extends AsyncTask<Void, Void, List<Movies>> {
    private final onFavoriteTaskCompleted mFavoriteTaskCompleted;
    private static final String TAG = "FavoriteThreadHandler";
    private Movie20Database mMovie20Database;


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
