/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.moview.Utils;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.amannawabi.moview.Data.Movie20Database;
import com.amannawabi.moview.Data.MovieDAO;
import com.amannawabi.moview.Model.Movies;

import java.util.List;

public class MovieRepository {
    private final LiveData<List<Movies>> mAllMovies;

    public MovieRepository(Application application) {
        Movie20Database db = Movie20Database.getInstance(application);
        MovieDAO movieDao = db.mMovieDAO();
        mAllMovies = movieDao.getAllMovies();

    }

    public LiveData<List<Movies>> getAllMovies(){
        return mAllMovies;
    }
}
