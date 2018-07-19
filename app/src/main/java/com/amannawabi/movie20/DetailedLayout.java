/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.movie20;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.amannawabi.movie20.Controller.MovieAdapter;
import com.amannawabi.movie20.Controller.ReviewAdapter;
import com.amannawabi.movie20.Controller.TrailerAdapter;
import com.amannawabi.movie20.Data.Movie20Database;
import com.amannawabi.movie20.Model.MovieViewModel;
import com.amannawabi.movie20.Model.Movies;
import com.amannawabi.movie20.Model.Review;
import com.amannawabi.movie20.Model.Trailer;
import com.amannawabi.movie20.Utils.FavoriteExecutor;
import com.amannawabi.movie20.Utils.ReviewThread;
import com.amannawabi.movie20.Utils.TrailerThread;
import com.amannawabi.movie20.Utils.NetworkUtils;
import com.amannawabi.movie20.Utils.onReviewTaskCompleted;
import com.amannawabi.movie20.Utils.onTrailerTaskCompleted;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;


public class DetailedLayout extends AppCompatActivity implements TrailerAdapter.ListItemClickListener,
        onTrailerTaskCompleted, onReviewTaskCompleted {
    private static final String TAG = "MovieDetailedLayout";
    private TextView mMovieTitle, mMovieRating, mMovieOverview, mMovieReleaseDate;
    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;
    private ImageView mMoviePoster;
    private String iMovieRating;
    private URL mTrailerUrl;
    private List<Trailer> sMovieTrailerRef;
    private static final String POSTER_PATH = "http://image.tmdb.org/t/p/w780//";
    //    private static String youtubeAdd = "https://www.youtube.com/watch?v=ue80QwXMRHg";
    private static Movie20Database mMovie20Database;

    private ToggleButton toggleButton;
    public MovieViewModel mMovieViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_layout);
        Log.d(TAG, "onCreate: starts");
        mTrailerRecyclerView = findViewById(R.id.trailer_rv);
        mReviewRecyclerView = findViewById(R.id.review_rv);
        mMovieTitle = findViewById(R.id.movie_title);
        mMoviePoster = findViewById(R.id.movie_poster);
        mMovieRating = findViewById(R.id.movie_rating);
        mMovieOverview = findViewById(R.id.movie_overview);
        mMovieReleaseDate = findViewById(R.id.movie_release_date);
//        mMovie20Database = Movie20Database.getInstance(getApplicationContext());
        mMovie20Database = MainActivity.mMovie20DB;
        toggleButton = findViewById(R.id.favorite_abtn);

        FavoriteExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final Movies movies = getIntent().getParcelableExtra("Detail Layout");
                if(mMovie20Database.mMovieDAO().ifExist(movies.getMovieId())){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.favorite));
                        }
                    });

                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.unfavorite));
                        }
                    });

                }
            }
        });
//        Movies movies = getIntent().getParcelableExtra("Detail Layout");
//        if (mMovie20Database.mMovieDAO().ifExist(movies.getMovieId())) {
//            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.favorite));
//
//        } else {
//            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.unfavorite));
//
//        }

//        toggleButton.setChecked(false);
//        Log.d(TAG, "onCreate: " + toggleButton.isChecked());
//        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Movies movies = getIntent().getParcelableExtra("Detail Layout");
//                if (isChecked) {
//                    mMovie20Database.mMovieDAO().addMovie(movies);
//                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.favorite));
//                } else {
//                    mMovie20Database.mMovieDAO().deleteMovie(movies);
//                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.unfavorite));
//                }
//
//            }
//        });
//
        toggleButton.setChecked(false);
        Log.d(TAG, "onCreate: " + toggleButton.isChecked());
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final Movies movies = getIntent().getParcelableExtra("Detail Layout");
                if (isChecked) {
                    FavoriteExecutor.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mMovie20Database.mMovieDAO().addMovie(movies);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.favorite));

                                }
                            });
                             }
                    });
                } else {
                    FavoriteExecutor.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mMovie20Database.mMovieDAO().deleteMovie(movies);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.unfavorite));

                                }
                            });
                             }
                    });
                }
            }
        });
        createDetailLayout();
    }

    /**
     * Sets the movie data into UI components in Detail Activity
     */
    private void createDetailLayout() {
        String RATING = "";
        Intent intent = getIntent();
        Movies movies = intent.getParcelableExtra("Detail Layout");
        iMovieRating = movies.getRatings();
        mMovieTitle.setText(movies.getMovieTitle());
        Picasso.get().load(POSTER_PATH + movies.getMoviePoster()).into(mMoviePoster);
        RATING = iMovieRating + "/10";
        mMovieRating.setText(RATING);
        mMovieReleaseDate.setText(movies.getReleaseDate().substring(0, 4));
        mMovieOverview.setText(movies.getMovieOverView());
        String iMovieID = Integer.toString(movies.getMovieId());
        createTrailerRecycler(iMovieID);
        createReviewRecycler(iMovieID);
//        Log.d(TAG, "createDetailLayout: Trailer URL " + mTrailerUrl);
//        Log.d(TAG, "createDetailLayout: " + movies.getMovieId());

    }


    private void createTrailerRecycler(String iMovieID) {
        mTrailerRecyclerView.setHasFixedSize(true);
        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        mTrailerUrl = NetworkUtils.buildTrailerURL(iMovieID);
        Log.d(TAG, "createTrailerRecycler: " + mTrailerUrl);
        boolean isNetworkConnected = NetworkUtils.isNetworkConnected(this);
        if (isNetworkConnected) {
            TrailerThread trailerQuery = new TrailerThread(DetailedLayout.this);
            trailerQuery.execute(mTrailerUrl);
        } else {
            Toast.makeText(DetailedLayout.this, "Network disconnected\n Please connect to internet", Toast.LENGTH_LONG).show();
        }

    }

    private void createReviewRecycler(String iMovieID) {
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        URL reviewUrl = NetworkUtils.buildReviewURL(iMovieID);
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
        RecyclerView.Adapter trailerAdapter = new TrailerAdapter(sMovieTrailerRef, DetailedLayout.this);
        mTrailerRecyclerView.setAdapter(trailerAdapter);
    }

    @Override
    public void onReviewTaskCompleted(List<Review> mReviewList) {
        RecyclerView.Adapter reviewAdapter = new ReviewAdapter(mReviewList);
        mReviewRecyclerView.setAdapter(reviewAdapter);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        String mKey = sMovieTrailerRef.get(clickedItemIndex).getsTrailerKey();
        String sTrailerYouTubeUrl = NetworkUtils.buildYouTubeURL(mKey);
//        Log.d(TAG, "onListItemClick: " +sTrailerYouTubeUrl);
        Uri mTrailerYouTubeUrl = Uri.parse(sTrailerYouTubeUrl);
//       Uri mTrailerYouTubeUrl = Uri.parse(youtubeAdd);
        Intent intent = new Intent(Intent.ACTION_VIEW, mTrailerYouTubeUrl);
//        Log.d(TAG, "onListItemClick: " +mTrailerYouTubeUrl);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
//            Toast.makeText(this, "Trailer Key " +"Index " +mTrailerYouTubeUrl, Toast.LENGTH_SHORT).show();

        }

    }
}
