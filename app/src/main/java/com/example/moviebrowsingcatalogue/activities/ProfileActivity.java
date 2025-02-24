package com.example.moviebrowsingcatalogue.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.moviebrowsingcatalogue.fragments.ProfileFragment;
import com.example.moviebrowsingcatalogue.activities.MoviesActivity;
import com.example.moviebrowsingcatalogue.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile"); // Set page title

        // Bottom Navigation Setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // ✅ Ensure "Profile" is selected when in ProfileActivity
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        // Load the default fragment
        loadFragment(new ProfileFragment());

        // Handle bottom navigation selection
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_movies) {
                    startActivity(new Intent(ProfileActivity.this, MoviesActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_tvshows) {
                    startActivity(new Intent(ProfileActivity.this, TvShowsActivity.class));
                    finish();
                    return true;
                }else if (itemId == R.id.nav_profile) {
                    return true; // Already in ProfileActivity
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
