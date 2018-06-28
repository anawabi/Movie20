/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.moview;


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

import com.amannawabi.moview.Controller.MovieAdapter;
import com.amannawabi.moview.Data.Movie20Database;
import com.amannawabi.moview.Model.MovieViewModel;
import com.amannawabi.moview.Model.Movies;
import com.amannawabi.moview.Utils.FavoriteThread;
import com.amannawabi.moview.Utils.MovieThread;
import com.amannawabi.moview.Utils.NetworkUtils;
import com.amannawabi.moview.Utils.onFavoriteTaskCompleted;
import com.amannawabi.moview.Utils.onTaskCompleted;
import com.facebook.stetho.Stetho;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements onFavoriteTaskCompleted,
        onTaskCompleted, MovieAdapter.ListItemClickListener {

    private RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    private static final String TAG = "MovieMainActivity";
    private static List<Movies> mMovieList = new ArrayList<>();
    private URL url;
    public static Movie20Database mMovie20DB;
    private MovieViewModel mMovieViewModel;
    private Parcelable mSavedMovies;
    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "recycler_state";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started");
        mRecyclerView = findViewById(R.id.movies_rv);
        mMovie20DB = Room.databaseBuilder(getApplicationContext(), Movie20Database.class, "movie20db").allowMainThreadQueries().build();

        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate: SavedInstance not Null");
            mSavedMovies = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mSavedMovies);

        } else {

            createRecycler("popular");
            Log.d(TAG, "onCreate: populating new data");
        }

        final MovieAdapter movieAdapter = new MovieAdapter(this);
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mMovieViewModel.getAllMovies().observe(this, new Observer<List<Movies>>() {
            @Override
            public void onChanged(@Nullable List<Movies> movies) {
                movieAdapter.setMoviess(movies);

            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        Log.d(TAG, "onSaveInstanceState: started");
        mSavedMovies = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(KEY_RECYCLER_STATE, mSavedMovies);
//        Log.d(TAG, "onSaveInstanceState: " +outState.getParcelable(KEY_RECYCLER_STATE));
        super.onSaveInstanceState(outState);
    }

    //
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: Started");
        Parcelable listState = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
        mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Started");
        // restore RecyclerView state
//        if (mBundleRecyclerViewState != null) {
//            Log.d(TAG, "onResume: " +mBundleRecyclerViewState);
//            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
//            mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: started");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Started");
        // save RecyclerView state
//        mBundleRecyclerViewState = new Bundle();
//        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
//        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop: started");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Started");
    }

    /**
     * Generates URL by sending the sort order parameter to Network Utils buildURL method and generates
     * Recycler view and populates the movie data by calling Async Task class.
     */
    private void createRecycler(String sortBy) {

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        url = NetworkUtils.buildURL(sortBy);
//        Log.d(TAG, "onCreate: URL Generated" + url);
        boolean isNetworkConnected = NetworkUtils.isNetworkConnected(this);
        if (isNetworkConnected) {
            MovieThread movieQuery = new MovieThread(MainActivity.this);
            movieQuery.execute(url);
        } else {
            createFavoriteRecycler();
            Toast.makeText(MainActivity.this, "Network disconnected\n Please connect to internet", Toast.LENGTH_LONG).show();
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
        return true;
    }

    /**
     * Enables the user to sort the movie data by Popularity and Highest Rating by providing selectable menu items
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItem = item.getItemId();
        if (selectedItem == R.id.sort_by_popular) {
            createRecycler("popular");
        } else if (selectedItem == R.id.sort_by_top_rated) {
            createRecycler("top_rated");
//            Log.d(TAG, "onOptionsItemSelected: " + url);
        } else if (selectedItem == R.id.favorites) {
            createFavoriteRecycler();
            Toast.makeText(MainActivity.this, "You Clicked Favorite", Toast.LENGTH_SHORT).show();
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

