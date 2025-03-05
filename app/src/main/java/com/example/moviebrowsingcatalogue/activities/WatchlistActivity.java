package com.example.moviebrowsingcatalogue.activities;

import android.os.Bundle;

import com.example.moviebrowsingcatalogue.R;

public class WatchlistActivity extends NavigationActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topmovies);

        setupNavigation(R.id.nav_top_movies, "Watchlists");
    }
}
