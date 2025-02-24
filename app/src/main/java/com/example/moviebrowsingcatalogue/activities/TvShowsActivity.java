package com.example.moviebrowsingcatalogue.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.RetrofitClient;
import com.example.moviebrowsingcatalogue.core.Movie;
import com.example.moviebrowsingcatalogue.services.ApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowsActivity extends AppCompatActivity {

    private LinearLayout moviesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TV Shows");

        moviesContainer = findViewById(R.id.moviesContainer);

        fetchTvShows();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_tvshows);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(TvShowsActivity.this, HomeActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_movies) {  // ✅ Navigates to Movies
                    startActivity(new Intent(TvShowsActivity.this, MoviesActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_tvshows) {
                    return true; // Already in TvShowsActivity
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(TvShowsActivity.this, ProfileActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    private void fetchTvShows() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Movie>> call = apiService.getTvShows();  // ✅ Calls the TV shows API

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> tvShows = response.body();
                    displayMovies(tvShows);
                } else {
                    Toast.makeText(TvShowsActivity.this, "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("TvShowsActivity", "API Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Toast.makeText(TvShowsActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                Log.e("TvShowsActivity", "Network Error: " + t.getMessage());
            }
        });
    }



    private void displayMovies(List<Movie> movies) {
        moviesContainer.removeAllViews();

        for (Movie movie : movies) {
            View movieView = LayoutInflater.from(this).inflate(R.layout.item_movie_dynamic, moviesContainer, false);

            TextView titleTextView = movieView.findViewById(R.id.titleTextView);
            TextView genreTextView = movieView.findViewById(R.id.genreTextView);
            TextView directorTextView = movieView.findViewById(R.id.directorTextView);
            ImageView coverImageView = movieView.findViewById(R.id.coverImageView);

            titleTextView.setText(movie.getTitle());
            genreTextView.setText("Genre: " + movie.getGenre());
            directorTextView.setText("Director: " + movie.getDirector());

            // This is used to load images from links
            Glide.with(this)
                    .load(movie.getCoverImage())
                    .into(coverImageView);

            moviesContainer.addView(movieView);
        }
    }
}
