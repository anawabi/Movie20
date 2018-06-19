/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.moview;


import android.arch.persistence.room.Room;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements onFavoriteTaskCompleted, onTaskCompleted, MovieAdapter.ListItemClickListener {

    private RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    private static final String TAG = "MovieMainActivity";
    private static List<Movies> mMovieList = new ArrayList<>();
    private URL url;
    public static Movie20Database mMovie20DB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);
//        Log.d(TAG, "onCreate: Started");
        recyclerView = findViewById(R.id.movies_rv);
        mMovie20DB = Room.databaseBuilder(getApplicationContext(), Movie20Database.class, "movie20db").allowMainThreadQueries().build();
        createRecycler("popular");
        Log.d(TAG, "onCreate: Saved Instance " + savedInstanceState);

    }

    /**
     * Generates URL by sending the sort order parameter to Network Utils buildURL method and generates
     * Recycler view and populates the movie data by calling Async Task class.
     */
    private void createRecycler(String sortBy) {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        url = NetworkUtils.buildURL(sortBy);
//        Log.d(TAG, "onCreate: URL Generated" + url);
        boolean isNetworkConnected = NetworkUtils.isNetworkConnected(this);
        if (isNetworkConnected) {
            MovieThread movieQuery = new MovieThread(MainActivity.this);
            movieQuery.execute(url);
        } else {
            Toast.makeText(MainActivity.this, "Network disconnected\n Please connect to internet", Toast.LENGTH_LONG).show();
        }

    }
    private void createFavoriteRecycler() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

       boolean isNetworkConnected = NetworkUtils.isNetworkConnected(this);
        if (isNetworkConnected) {
            FavoriteThread favoriteQuery = new FavoriteThread(MainActivity.this);
            favoriteQuery.execute();
        } else {
            Toast.makeText(MainActivity.this, "Network disconnected\n Please connect to internet", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onTaskCompleted(List<Movies> movies) {
//        Log.d(TAG, "onTaskCompleted: " + movies.size());
        mMovieList = movies;
        mAdapter = new MovieAdapter(mMovieList, MainActivity.this);
//        Log.d(TAG, "onCreate: " + mMovieList.size());
        recyclerView.setAdapter(mAdapter);
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
        }else if (selectedItem == R.id.favorites){
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
        recyclerView.setAdapter(mAdapter);
//        Log.d(TAG, "onPostExecute: " + mMovieList.size());
    }
}

