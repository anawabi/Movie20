/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.moview.Model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.amannawabi.moview.Utils.MovieRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    MovieRepository mRepository;
    LiveData<List<Movies>> mAllMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MovieRepository(application);
        mAllMovies = mRepository.getAllMovies();
    }

    public LiveData<List<Movies>> getAllMovies(){
        return mAllMovies;
    }
}
