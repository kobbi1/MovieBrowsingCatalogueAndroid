package com.example.moviebrowsingcatalogue.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.moviebrowsingcatalogue.fragments.HomeFragment;
import com.example.moviebrowsingcatalogue.activities.MoviesActivity;
import com.example.moviebrowsingcatalogue.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home"); // Set page title

        // Bottom Navigation Setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // ✅ Ensure "Home" is selected when in HomeActivity
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Load the default fragment
        loadFragment(new HomeFragment());

        // Handle bottom navigation selection
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    return true; // Already in HomeActivity
                } else if (itemId == R.id.nav_movies) {
                    startActivity(new Intent(HomeActivity.this, MoviesActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_tvshows) {
                    startActivity(new Intent(HomeActivity.this, TvShowsActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
