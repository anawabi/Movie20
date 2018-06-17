/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.moview;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amannawabi.moview.Controller.ReviewAdapter;
import com.amannawabi.moview.Controller.TrailerAdapter;
import com.amannawabi.moview.Data.Movie20Database;
import com.amannawabi.moview.Model.Movies;
import com.amannawabi.moview.Model.Review;
import com.amannawabi.moview.Model.Trailer;
import com.amannawabi.moview.Utils.ReviewThread;
import com.amannawabi.moview.Utils.TrailerThread;
import com.amannawabi.moview.Utils.NetworkUtils;
import com.amannawabi.moview.Utils.onReviewTaskCompleted;
import com.amannawabi.moview.Utils.onTrailerTaskCompleted;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ConcurrentModificationException;
import java.util.List;


public class DetailedLayout extends AppCompatActivity implements TrailerAdapter.ListItemClickListener, onTrailerTaskCompleted, onReviewTaskCompleted {
    private static final String TAG = "MovieDetailedLayout";
    private TextView mMovieTitle, mMovieRating, mMovieOverview, mMovieReleaseDate;
    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;
    private RecyclerView.Adapter mTrailerAdapter;
    private RecyclerView.Adapter mReviewAdapter;
    private ImageView mMoviePoster;
    private String iMovieRating;
    private URL trailerUrl;
    private URL reviewUrl;
    private List<Trailer> sMovieTrailerRef;
    private static final String POSTER_PATH = "http://image.tmdb.org/t/p/w780//";
//    public static Movie20Database mMovie20Database;
    private FloatingActionButton mFavorite;
    boolean isFavorite = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_layout);
        mTrailerRecyclerView = findViewById(R.id.trailer_rv);
        mReviewRecyclerView = findViewById(R.id.review_rv);
        mMovieTitle = findViewById(R.id.movie_title);
        mMoviePoster = findViewById(R.id.movie_poster);
        mMovieRating = findViewById(R.id.movie_rating);
        mMovieOverview = findViewById(R.id.movie_overview);
        mMovieReleaseDate = findViewById(R.id.movie_release_date);
        mFavorite = findViewById(R.id.favorite_abtn);
        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movies movies = getIntent().getParcelableExtra("Detail Layout");
               if(!isFavorite) {
                   MainActivity.mMovie20DB.mMovieDAO().addMovie(movies);
                   mFavorite.setBackgroundResource(R.drawable.favorite);
                   Toast.makeText(DetailedLayout.this, "Movie Added to Favorite List", Toast.LENGTH_LONG).show();
                    isFavorite=true;
               } else{
                   MainActivity.mMovie20DB.mMovieDAO().deleteMovie(movies);
                   mFavorite.setBackgroundResource(R.drawable.unfavorite);
                   Toast.makeText(DetailedLayout.this, "Movie Deleted from Favorite List", Toast.LENGTH_SHORT).show();
                    isFavorite=false;
               }
            }
        });


        createDetailLayout();

    }


    /**
     * Sets the movie data into UI components in Detail Activity
     */
    private void createDetailLayout() {
        final String RATING = "/10";
        Intent intent = getIntent();
        Movies movies = intent.getParcelableExtra("Detail Layout");
        iMovieRating = movies.getRatings();
        mMovieTitle.setText(movies.getMovieTitle());
        Picasso.get().load(POSTER_PATH + movies.getMoviePoster()).into(mMoviePoster);
        mMovieRating.setText(iMovieRating + RATING);
        mMovieReleaseDate.setText(movies.getReleaseDate().substring(0, 4));
        mMovieOverview.setText(movies.getMovieOverView());
        String iMovieID = Integer.toString(movies.getMovieId());


        createTrailerRecycler(iMovieID);
        createReviewRecycler(iMovieID);
        Log.d(TAG, "createDetailLayout: Trailer URL " + trailerUrl);
        Log.d(TAG, "createDetailLayout: " + movies.getMovieId());

//        https://www.youtube.com/watch?v=ue80QwXMRHg
    }


    private void createTrailerRecycler(String iMovieID){
        mTrailerRecyclerView.setHasFixedSize(true);
        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        trailerUrl = NetworkUtils.buildTrailerURL(iMovieID);
        boolean isNetworkConnected = NetworkUtils.isNetworkConnected(this);
        if (isNetworkConnected) {
            TrailerThread trailerQuery = new TrailerThread(DetailedLayout.this);
            trailerQuery.execute(trailerUrl);
        } else {
            Toast.makeText(DetailedLayout.this, "Network disconnected\n Please connect to internet", Toast.LENGTH_LONG).show();
        }

    }

    private void createReviewRecycler(String iMovieID) {
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        reviewUrl = NetworkUtils.buildReviewURL(iMovieID);
        boolean isNetworkConnected = NetworkUtils.isNetworkConnected(this);
        if (isNetworkConnected) {
            ReviewThread reviewQuery = new ReviewThread(DetailedLayout.this);

            reviewQuery.execute(reviewUrl);
        } else {
            Toast.makeText(DetailedLayout.this, "Network disconnected\n Please connect to internet", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onTrailerTaskCompleted(List<Trailer> mTrailerList) {

        sMovieTrailerRef = mTrailerList;
//        Log.d(TAG, "onTaskCompleted2: " + sMovieTrailerRef.size());
        mTrailerAdapter = new TrailerAdapter(sMovieTrailerRef, DetailedLayout.this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
    }

    @Override
    public void onReviewTaskCompleted(List<Review> mReviewList) {
        mReviewAdapter = new ReviewAdapter(mReviewList);
        mReviewRecyclerView.setAdapter(mReviewAdapter);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        String mKey = sMovieTrailerRef.get(clickedItemIndex).getsTrailerKey();
        RecyclerView.ViewHolder mKeys = mTrailerRecyclerView.findViewHolderForItemId(clickedItemIndex);

        Toast.makeText(this, "Trailer Key " +"Index " +clickedItemIndex + " " + mKeys, Toast.LENGTH_SHORT).show();
    }
}
