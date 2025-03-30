package com.example.moviebrowsingcatalogue.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.RetrofitClient;
import com.example.moviebrowsingcatalogue.adapters.WatchlistItemsAdapter;
import com.example.moviebrowsingcatalogue.core.Movie;
import com.example.moviebrowsingcatalogue.core.UserWatchlist;
import com.example.moviebrowsingcatalogue.core.Watchlist;
import com.example.moviebrowsingcatalogue.core.WatchlistItems;
import com.example.moviebrowsingcatalogue.fragments.MoviesDetailFragment;
import com.example.moviebrowsingcatalogue.services.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchlistItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WatchlistItemsAdapter adapter;
    private List<WatchlistItems> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist_items);

        recyclerView = findViewById(R.id.watchlistRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new WatchlistItemsAdapter(items, item -> {
            int movieId = (int) item.getId();

            // 👇 Option 1: Navigate to MovieDetailFragment inside this activity (if fragment container exists)
            /*
            MoviesDetailFragment fragment = MoviesDetailFragment.newInstance(movieId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment) // make sure this ID exists in your layout
                    .addToBackStack(null)
                    .commit();
            */

            // 👇 Option 2: Start NavigationActivity and pass movieId to open MovieDetailFragment
            Intent intent = new Intent(this, NavigationActivity.class);
            intent.putExtra("movie_id", movieId);
            intent.putExtra("navigate_to", "MovieDetailFragment");
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
        fetchWatchlistItems();
    }

    private void fetchWatchlistItems() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        long userId = prefs.getLong("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<UserWatchlist> call = apiService.getUserWatchlists(userId);

        call.enqueue(new Callback<UserWatchlist>() {
            @Override
            public void onResponse(Call<UserWatchlist> call, Response<UserWatchlist> response) {
                if (response.isSuccessful() && response.body() != null) {
                    items.clear();
                    for (Watchlist watchlist : response.body().getUserWatchlists()) {
                        List<Movie> movies = watchlist.getMovies();
                        if (movies != null) {
                            for (Movie movie : movies) {
                                items.add(new WatchlistItems(
                                        movie.getId(),
                                        movie.getTitle(),
                                        movie.getDescription(),
                                        movie.getCoverImage(),
                                        false // Mark TV show = true if needed
                                ));
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(WatchlistItemsActivity.this, "Failed to load watchlists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserWatchlist> call, Throwable t) {
                Toast.makeText(WatchlistItemsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
