package com.amannawabi.moview.Data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.amannawabi.moview.Model.Movies;

@Database(entities = Movies.class, version = 1)
public abstract class Movie20Database extends RoomDatabase {

    public abstract MovieDAO mMovieDAO();
}
