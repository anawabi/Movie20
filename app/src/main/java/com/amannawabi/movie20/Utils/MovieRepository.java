/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.movie20.Utils;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.amannawabi.movie20.Data.Movie20Database;
import com.amannawabi.movie20.Data.MovieDAO;
import com.amannawabi.movie20.Model.Movies;

import java.util.List;

public class MovieRepository {
    private final MovieDAO mMovieDAO;
    private final LiveData<List<Movies>> mAllMovies;
    private static final String TAG = "MovieRepository";

    public MovieRepository(Application application) {
        Movie20Database db = Movie20Database.getInstance(application);
        mMovieDAO = db.mMovieDAO();
        mAllMovies = mMovieDAO.getAllMovies();

    }

    public LiveData<List<Movies>> getAllMovies(){
        Log.d(TAG, "getAllMovies: " +mAllMovies.toString());
        return mAllMovies;
    }
    public void insertMovie(Movies movies){
     new insertAsyncTask(mMovieDAO).execute(movies);
    }

    private static class insertAsyncTask extends AsyncTask<Movies, Void, Void>{

        private final MovieDAO mAsyncDAO;

        insertAsyncTask(MovieDAO movieDAO){
            mAsyncDAO = movieDAO;
        }

        @Override
        protected Void doInBackground(Movies... movies) {
            mAsyncDAO.addMovie(movies[0]);

            return null;
        }
    }
}
