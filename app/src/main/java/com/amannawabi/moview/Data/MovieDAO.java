package com.amannawabi.moview.Data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

import com.amannawabi.moview.Model.Movies;

@Dao
public interface MovieDAO {

    @Insert
    public void addMovie(Movies movies);

    @Delete
    public void deleteMovie(Movies movies);

}
