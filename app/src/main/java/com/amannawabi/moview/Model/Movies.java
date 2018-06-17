/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.moview.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Generates Data Model class and sets the setter and getter methods and send the data by implementing
 * parcelable interface
 */
@Entity (tableName = "movies")
public class Movies implements Parcelable {

    @PrimaryKey
    private int mMovieId;
    @ColumnInfo(name = "movie_title")
    private String mMovieTitle;
    @ColumnInfo(name = "movie_poster")
    private String mMoviePoster;
    @ColumnInfo(name = "movie_overview")
    private String mMovieOverView;
    @ColumnInfo(name = "movie_ratings")
    private String mRatings;
    @ColumnInfo(name = "movie_release_date")
    private String mReleaseDate;

    @Ignore
    public Movies(int mMovieId, String movieTitle, String moviePoster, String movieOverView, String ratings, String releaseDate) {
        this.mMovieId = mMovieId;
        this.mMovieTitle = movieTitle;
        this.mMoviePoster = moviePoster;
        this.mMovieOverView = movieOverView;
        this.mRatings = ratings;
        this.mReleaseDate = releaseDate;

    }

    protected Movies(Parcel in) {
        mMovieId = in.readInt();
        mMovieTitle = in.readString();
        mMoviePoster = in.readString();
        mMovieOverView = in.readString();
        mRatings = in.readString();
        mReleaseDate = in.readString();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMovieId);
        dest.writeString(mMovieTitle);
        dest.writeString(mMoviePoster);
        dest.writeString(mMovieOverView);
        dest.writeString(mRatings);
        dest.writeString(mReleaseDate);
    }

    public int getMovieId() {
        return mMovieId;
    }

    public void setMovieId(int movieId) {
        mMovieId = movieId;
    }

    public Movies() {
    }


    public String getMovieTitle() {
        return mMovieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.mMovieTitle = movieTitle;
    }

    public String getMoviePoster() {
        return mMoviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.mMoviePoster = moviePoster;
    }

    public String getMovieOverView() {
        return mMovieOverView;
    }

    public void setMovieOverView(String movieOverView) {
        this.mMovieOverView = movieOverView;
    }

    public String getRatings() {
        return mRatings;
    }

    public void setRatings(String ratings) {
        this.mRatings = ratings;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
    }


}
