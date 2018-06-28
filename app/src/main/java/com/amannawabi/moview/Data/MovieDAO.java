package com.amannawabi.moview.Data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.amannawabi.moview.Model.Movies;

import java.util.List;

@Dao
public interface MovieDAO {
    @Query("select * from movies")
     List<Movies> viewMovie();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     void addMovie(Movies movies);



    @Delete
    void deleteMovie(Movies movies);

    @Query("SELECT * FROM movies WHERE mMovieId=:id")
    boolean ifExist (int id);

    @Query("select * from movies")
    LiveData<List<Movies>> getAllMovies();
}
