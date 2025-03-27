package com.example.moviebrowsingcatalogue.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.RetrofitClient;
import com.example.moviebrowsingcatalogue.core.Movie;
import com.example.moviebrowsingcatalogue.core.UserWatchlist;
import com.example.moviebrowsingcatalogue.core.Watchlist;
import com.example.moviebrowsingcatalogue.entities.MovieEntity;
import com.example.moviebrowsingcatalogue.entities.WatchlistEntity;
import com.example.moviebrowsingcatalogue.entities.WatchlistItemEntity;
import com.example.moviebrowsingcatalogue.services.ApiService;
import com.example.moviebrowsingcatalogue.storage.MbcDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWatchlistsFragment extends Fragment {

    private LinearLayout watchlistsContainer;
    private ApiService apiService;
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_watchlists, container, false);
        watchlistsContainer = view.findViewById(R.id.watchlistsContainer);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        prefs = requireActivity().getSharedPreferences("UserPrefs", requireActivity().MODE_PRIVATE);

        long userId = prefs.getLong("userId", -1);
        if (userId == -1) {
            Toast.makeText(getActivity(), "User ID missing. Please login.", Toast.LENGTH_SHORT).show();
            return view;
        }

        fetchWatchlists(userId);
        return view;
    }


    private void fetchWatchlists(long userId) {
        Call<UserWatchlist> call = apiService.getUserWatchlists(userId);

        call.enqueue(new Callback<UserWatchlist>() {
            @Override
            public void onResponse(Call<UserWatchlist> call, Response<UserWatchlist> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Watchlist> watchlists = response.body().getUserWatchlists();

                    for (Watchlist watchlist : watchlists) {
                        TextView watchlistText = new TextView(getActivity());
                        watchlistText.setText("• " + watchlist.getName());
                        watchlistText.setTextSize(18f);
                        watchlistText.setPadding(0, 8, 0, 8);
                        watchlistText.setGravity(Gravity.START);

                        watchlistsContainer.addView(watchlistText);


                        saveToLocalDatabase(watchlists, userId);
                    }

                    if (watchlists.isEmpty()) {
                        TextView emptyText = new TextView(getActivity());
                        emptyText.setText("You have no watchlists yet.");
                        emptyText.setTextSize(16f);
                        emptyText.setPadding(0, 16, 0, 0);
                        watchlistsContainer.addView(emptyText);
                    }

                } else {
                    Toast.makeText(getActivity(), "Failed to load watchlists: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserWatchlist> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }


    private void saveToLocalDatabase(List<Watchlist> watchlists, long userId) {
        MbcDatabase db = Room.databaseBuilder(
                requireContext(),
                MbcDatabase.class,
                "mbc_database"
        ).build();

        new Thread(() -> {
            List<WatchlistEntity> watchlistEntities = new ArrayList<>();
            List<MovieEntity> movieEntities = new ArrayList<>();
            List<WatchlistItemEntity> joinItems = new ArrayList<>();
            Set<Long> movieIdsSeen = new HashSet<>();

            for (Watchlist watchlist : watchlists) {
                WatchlistEntity watchlistEntity = new WatchlistEntity();
                watchlistEntity.id = watchlist.getId();
                watchlistEntity.name = watchlist.getName();
                watchlistEntity.userId = userId;
                watchlistEntities.add(watchlistEntity);

                List<Movie> movies = watchlist.getMovies();
                if(movies != null) {
                    for (Movie movie : watchlist.getMovies()) {
                        if (!movieIdsSeen.contains((long) movie.getId())) {
                            MovieEntity movieEntity = new MovieEntity();
                            movieEntity.id = movie.getId();
                            movieEntity.title = movie.getTitle();
                            movieEntity.description = movie.getDescription();
                            movieEntities.add(movieEntity);
                            movieIdsSeen.add((long) movie.getId());
                        }

                        WatchlistItemEntity item = new WatchlistItemEntity();
                        item.watchlistId = watchlist.getId();
                        item.movieId = movie.getId();
                        joinItems.add(item);
                    }
                }
            }

            db.watchlistStorage().insertWatchlists(watchlistEntities);
            db.watchlistStorage().insertMovies(movieEntities);
            db.watchlistStorage().insertWatchlistItems(joinItems);
        }).start();
    }


}
