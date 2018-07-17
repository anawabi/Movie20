/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.moview.Model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.amannawabi.moview.Utils.MovieRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private static final String TAG = "MovieViewModel";
    private final LiveData<List<Movies>> mAllMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        MovieRepository repository = new MovieRepository(application);
        mAllMovies = repository.getAllMovies();
    }

    public LiveData<List<Movies>> getAllMovies(){
//        Log.d(TAG, "getAllMovies: invoked " +mAllMovies.toString());
        return mAllMovies;
    }
}
