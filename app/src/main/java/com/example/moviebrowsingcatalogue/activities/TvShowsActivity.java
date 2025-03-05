package com.example.moviebrowsingcatalogue.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.RetrofitClient;
import com.example.moviebrowsingcatalogue.core.Movie;
import com.example.moviebrowsingcatalogue.services.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowsActivity extends NavigationActivity {

    private LinearLayout tvShowsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        setupNavigation(R.id.nav_tvshows, "TV Shows");
        tvShowsContainer = findViewById(R.id.moviesContainer);
        fetchTvShows();
    }

    private void fetchTvShows() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Movie>> call = apiService.getTvShows();

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> tvShows = response.body();
                    displayTvShows(tvShows);
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

    private void displayTvShows(List<Movie> tvShows) {
        tvShowsContainer.removeAllViews();

        for (Movie tvShow : tvShows) {
            View tvShowView = LayoutInflater.from(this).inflate(R.layout.item_movie_dynamic, tvShowsContainer, false);

            TextView titleTextView = tvShowView.findViewById(R.id.titleTextView);
            TextView genreTextView = tvShowView.findViewById(R.id.genreTextView);
            TextView directorTextView = tvShowView.findViewById(R.id.directorTextView);
            ImageView coverImageView = tvShowView.findViewById(R.id.coverImageView);

            titleTextView.setText(tvShow.getTitle());
            genreTextView.setText("Genre: " + tvShow.getGenre());
            directorTextView.setText("Director: " + tvShow.getDirector());

            Glide.with(this)
                    .load(tvShow.getCoverImage())
                    .into(coverImageView);

            tvShowsContainer.addView(tvShowView);
        }
    }
}
