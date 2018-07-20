/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.movie20.Data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.amannawabi.movie20.Model.Movies;

@Database(entities = Movies.class, version = 1_1, exportSchema = false)
public abstract class Movie20Database extends RoomDatabase {

    private static final String DB_NAME = "favorite_movies";
    private static Movie20Database mInstance;
    public static Movie20Database getInstance(Context context){
        if(mInstance == null){
            synchronized (Movie20Database.class){
                if (mInstance == null){
                    mInstance = Room.databaseBuilder(context.getApplicationContext(),
                            Movie20Database.class, Movie20Database.DB_NAME)
//                            .allowMainThreadQueries()
                            .build();
                }
            }
        }

        return mInstance;
    }

    public abstract MovieDAO mMovieDAO();
}
