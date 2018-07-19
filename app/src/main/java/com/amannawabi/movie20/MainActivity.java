/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.movie20;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amannawabi.movie20.Controller.MovieAdapter;
import com.amannawabi.movie20.Data.Movie20Database;
import com.amannawabi.movie20.Model.MovieViewModel;
import com.amannawabi.movie20.Model.Movies;
import com.amannawabi.movie20.Utils.FavoriteExecutor;
import com.amannawabi.movie20.Utils.FavoriteThread;
import com.amannawabi.movie20.Utils.MovieThread;
import com.amannawabi.movie20.Utils.NetworkUtils;
import com.amannawabi.movie20.Utils.onFavoriteTaskCompleted;
import com.amannawabi.movie20.Utils.onTaskCompleted;
import com.facebook.stetho.Stetho;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements onFavoriteTaskCompleted,
        onTaskCompleted, MovieAdapter.ListItemClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private static final String TAG = "MovieMainActivity";
    private List<Movies> mMovieList = new ArrayList<>();
    public static Movie20Database mMovie20DB;
    private final String SELECTED_MENU_ITEM = "selecteditem";
    private static String mSelectedMenuItem;
    private final String RECYCLER_POSITION_KEY = "recycler_position";
    private int mPosition = RecyclerView.NO_POSITION;
    private static Bundle mBundleState;
    private GridLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);
//        Log.d(TAG, "onCreate: Started");
        mRecyclerView = findViewById(R.id.movies_rv);
        mLayoutManager = new GridLayoutManager(this, 2);
//        mMovie20DB = Room.databaseBuilder(getApplicationContext(), Movie20Database.class, "favorite_movies")
//                .build();
        mMovie20DB = Movie20Database.getInstance(this);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RECYCLER_POSITION_KEY)) {
                mPosition = savedInstanceState.getInt(RECYCLER_POSITION_KEY);
                if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
                mRecyclerView.smoothScrollToPosition(mPosition);
            }
          String mSavedMenuItem = savedInstanceState.getString(SELECTED_MENU_ITEM);
//            Log.d(TAG, "onCreate: selected menu " +mSavedMenuItem);
            if (mSavedMenuItem != null) {
                createRecycler(mSavedMenuItem);
            }
            else {
                createRecycler("popular");
            }
        }
        else {
            createRecycler("popular");
        }
//
////        final MovieAdapter movieAdapter = new MovieAdapter(this);
//        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
//        movieViewModel.getAllMovies().observe(this, new Observer<List<Movies>>() {
//            @Override
//            public void onChanged(@Nullable List<Movies> movies) {
//                mAdapter = new MovieAdapter(movies, MainActivity.this);
//
////                Log.d(TAG, "onChanged: called in Oncreate");
//
//            }
//        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(RECYCLER_POSITION_KEY,  mLayoutManager.findFirstCompletelyVisibleItemPosition());
        outState.putString(SELECTED_MENU_ITEM, mSelectedMenuItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBundleState = new Bundle();
        int mRVPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
        Log.d(TAG, "onPause: " +mRVPosition);
        mBundleState.putInt(RECYCLER_POSITION_KEY, mRVPosition);

    }

    @Override
    protected void onResume() {
        super.onResume();
//
        if (mBundleState != null) {
            if (mBundleState.containsKey(RECYCLER_POSITION_KEY)) {
                int mRVPosition = mBundleState.getInt(RECYCLER_POSITION_KEY);
                if (mRVPosition == RecyclerView.NO_POSITION) mRVPosition = 0;
                // Scroll the RecyclerView to mPosition
                Log.d(TAG, "onResume: RV Position" +mRVPosition);
                mRecyclerView.smoothScrollToPosition(mRVPosition);
            }
        }
        Log.d(TAG, "onResume: Selected Menu 0" +mSelectedMenuItem);
        if (mSelectedMenuItem == "Favorite") {
            Log.d(TAG, "onResume: Selected Menu" +mSelectedMenuItem);
            Toast.makeText(this, "Favorite Menu Selected", Toast.LENGTH_SHORT).show();
            FavoriteExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    final List<Movies> movies = mMovie20DB.mMovieDAO().viewMovie();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final MovieAdapter movieAdapter = new MovieAdapter(MainActivity.this);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    mAdapter = new MovieAdapter(movies, MainActivity.this);
                                    movieAdapter.setMovies(movies);
//
                                    mRecyclerView.setAdapter(movieAdapter);
                                }
                            });

                        }
                    });
                }
            });
        }

    }


    /**
     * Generates URL by sending the sort order parameter to Network Utils buildURL method and generates
     * Recycler view and populates the movie data by calling Async Task class.
     */
    private void createRecycler(String sortBy) {

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        URL url = NetworkUtils.buildURL(sortBy);
//        Log.d(TAG, "onCreate: URL Generated" + url);
        boolean isNetworkConnected = NetworkUtils.isNetworkConnected(this);
        if (isNetworkConnected) {
            MovieThread movieQuery = new MovieThread(MainActivity.this);
            movieQuery.execute(url);
        } else {
            String sNetworkStatus = getString(R.string.network_status);
            createFavoriteRecycler();
            Toast.makeText(MainActivity.this, sNetworkStatus, Toast.LENGTH_LONG).show();
        }

    }

    private void createFavoriteRecycler() {

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        FavoriteThread favoriteQuery = new FavoriteThread(MainActivity.this);
        favoriteQuery.execute();

    }

    @Override
    public void onTaskCompleted(List<Movies> movies) {
//        Log.d(TAG, "onTaskCompleted: " + movies.size());

        mMovieList = movies;
        mAdapter = new MovieAdapter(mMovieList, MainActivity.this);
//        Log.d(TAG, "onCreate: " + mMovieList.size());
        mRecyclerView.setAdapter(mAdapter);
//        Log.d(TAG, "onPostExecute: " + mMovieList.size());
    }


    /**
     * Opens Detail Activity once the movie poster is clicked.
     * sends movie data to detail activity through intent.putExtra method
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(MainActivity.this, DetailedLayout.class);
        intent.putExtra("Detail Layout", mMovieList.get(clickedItemIndex));
        startActivity(intent);
    }

    /**
     * Populates the Menu in the action bar of the activity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.sort_by_popular);
        menuItem.setChecked(true);
        return true;
    }

    /**
     * Enables the user to sort the movie data by Popularity and Highest Rating by providing selectable menu items
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItem = item.getItemId();
        item.setChecked(false);
        switch (selectedItem) {
            case R.id.sort_by_popular:
                createRecycler("popular");
                mSelectedMenuItem = item.getTitle().toString();
//            mSelectedMenuItem="popular";
                break;
            case R.id.sort_by_top_rated:
                createRecycler("top_rated");
//                mSelectedMenuItem = "top_rated";
                mSelectedMenuItem = item.getTitle().toString();
//            Log.d(TAG, "onOptionsItemSelected: " + url);
                break;
            case R.id.favorites:
                createFavoriteRecycler();
                mSelectedMenuItem = "Favorite";
//                mSelectedMenuItem = item.getTitle().toString();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFavoriteTaskCompleted(List<Movies> movies) {//        Log.d(TAG, "onTaskCompleted: " + movies.size());
        mMovieList = movies;
        mAdapter = new MovieAdapter(mMovieList, MainActivity.this);
//        Log.d(TAG, "onCreate: " + mMovieList.size());
        mRecyclerView.setAdapter(mAdapter);
//        Log.d(TAG, "onPostExecute: " + mMovieList.size());
    }
}

