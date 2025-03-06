package com.example.moviebrowsingcatalogue.activities;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.fragments.MoviesFragment;

public class TvShowsActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);  // ✅ Reusing the same layout

        setupNavigation(R.id.nav_tvshows, "TV Shows");

        loadFragment(new MoviesFragment("tvshows"));
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
