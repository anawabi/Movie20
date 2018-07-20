/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.movie20.Model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.amannawabi.movie20.Utils.MovieRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private static final String TAG = "MovieViewModel";
    private final MovieRepository mRepository;
    private final LiveData<List<Movies>> mAllMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MovieRepository(application);
        mAllMovies = mRepository.getAllMovies();
    }

    public LiveData<List<Movies>> getAllMovies(){
//        Log.d(TAG, "getAllMovies: invoked " +mAllMovies.toString());
        return mAllMovies;
    }
    public void insertMovie(Movies movies){
        mRepository.insertMovie(movies);
    }
}
