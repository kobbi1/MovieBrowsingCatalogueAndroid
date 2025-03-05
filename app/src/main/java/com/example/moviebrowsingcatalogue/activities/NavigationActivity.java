package com.example.moviebrowsingcatalogue.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.moviebrowsingcatalogue.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class NavigationActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupNavigation(int selectedNavItemId, String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (navigationView != null) {
            navigationView.post(() -> navigationView.setCheckedItem(selectedNavItemId));
        }

        if (selectedNavItemId == R.id.nav_top_movies ||
                selectedNavItemId == R.id.nav_watchlist ||
                selectedNavItemId == R.id.nav_settings) {

            bottomNavigationView.getMenu().setGroupCheckable(0, true, false);
            for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                bottomNavigationView.getMenu().getItem(i).setChecked(false);
            }
            bottomNavigationView.getMenu().setGroupCheckable(0, true, true);
        } else {
            bottomNavigationView.setSelectedItemId(selectedNavItemId);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_top_movies) {
                if (!(this instanceof TopMoviesActivity)) {
                    startActivity(new Intent(this, TopMoviesActivity.class));
                    finish();
                }
                return true;
            } else if (id == R.id.nav_watchlist) {
                if (!(this instanceof WatchlistActivity)) {
                    startActivity(new Intent(this, WatchlistActivity.class));
                    finish();
                }
                return true;
            } else if (id == R.id.nav_settings) {
                if (!(this instanceof SettingsActivity)) {
                    startActivity(new Intent(this, SettingsActivity.class));
                    finish();
                }
                return true;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home && !(this instanceof HomeActivity)) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_movies && !(this instanceof MoviesActivity)) {
                startActivity(new Intent(this, MoviesActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_tvshows && !(this instanceof TvShowsActivity)) {
                startActivity(new Intent(this, TvShowsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_profile && !(this instanceof ProfileActivity)) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
