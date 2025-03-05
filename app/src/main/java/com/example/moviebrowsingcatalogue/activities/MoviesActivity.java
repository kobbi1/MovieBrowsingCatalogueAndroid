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

public class MoviesActivity extends NavigationActivity {

    private LinearLayout moviesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        setupNavigation(R.id.nav_movies, "Movies");

        moviesContainer = findViewById(R.id.moviesContainer);
        fetchMovies();
    }

    private void fetchMovies() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Movie>> call = apiService.getMovies();  // Calls the movies API

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body();
                    displayMovies(movies);
                } else {
                    Toast.makeText(MoviesActivity.this, "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("MoviesActivity", "API Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Toast.makeText(MoviesActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                Log.e("MoviesActivity", "Network Error: " + t.getMessage());
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

            Glide.with(this)
                    .load(movie.getCoverImage())
                    .into(coverImageView);

            moviesContainer.addView(movieView);
        }
    }
}