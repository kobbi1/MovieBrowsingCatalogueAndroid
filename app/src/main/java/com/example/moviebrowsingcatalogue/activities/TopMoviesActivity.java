package com.example.moviebrowsingcatalogue.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.RetrofitClient;
import com.example.moviebrowsingcatalogue.core.Movie;
import com.example.moviebrowsingcatalogue.services.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopMoviesActivity extends NavigationActivity {

    private LinearLayout moviesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topmovies);

        setupNavigation(R.id.nav_top_movies, "Top Movies");

        moviesContainer = findViewById(R.id.moviesContainer);

        // ✅ Fetch top movies when activity starts
        fetchTopMovies();
    }

    private void fetchTopMovies() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Movie>> call = apiService.getTopMovies();

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> topMovies = response.body();
                    displayMovies(topMovies);
                } else {
                    Toast.makeText(TopMoviesActivity.this, "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("TopMoviesActivity", "API Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Toast.makeText(TopMoviesActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                Log.e("TopMoviesActivity", "Network Error: " + t.getMessage());
            }
        });
    }

    private void displayMovies(List<Movie> movies) {
        moviesContainer.removeAllViews();

        if (movies == null || movies.isEmpty()) {
            TextView noMoviesTextView = new TextView(this);
            noMoviesTextView.setText("No movies have a rating");
            noMoviesTextView.setTextSize(18);
            noMoviesTextView.setPadding(16, 16, 16, 16);
            noMoviesTextView.setGravity(android.view.Gravity.CENTER);

            moviesContainer.addView(noMoviesTextView);
            return;
        }

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
