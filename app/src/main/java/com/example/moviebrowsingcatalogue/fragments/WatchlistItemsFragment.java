package com.example.moviebrowsingcatalogue.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.RetrofitClient;
import com.example.moviebrowsingcatalogue.adapters.WatchlistItemsAdapter;
import com.example.moviebrowsingcatalogue.core.Movie;
import com.example.moviebrowsingcatalogue.core.WatchlistItems;
import com.example.moviebrowsingcatalogue.services.ApiService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class WatchlistItemsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView titleView;
    private WatchlistItemsAdapter adapter;
    private final List<WatchlistItems> items = new ArrayList<>();

    private long watchlistId = -1;
    private String watchlistName = "";

    public WatchlistItemsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watchlist_items, container, false);

        recyclerView = view.findViewById(R.id.watchlistItemsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new WatchlistItemsAdapter(items, selectedItem -> {
            int movieId = (int) selectedItem.getId(); // assuming ID is integer
            MoviesDetailFragment fragment = MoviesDetailFragment.newInstance(movieId);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(adapter);

        titleView = view.findViewById(R.id.watchlistTitle);

        if (getArguments() != null) {
            watchlistId = getArguments().getLong("watchlistId", -1);
            watchlistName = getArguments().getString("watchlistName", "");
        }

        if (watchlistId == -1) {
            Toast.makeText(getContext(), "Missing watchlist ID", Toast.LENGTH_SHORT).show();
            return view;
        }

        titleView.setText(watchlistName);

        fetchWatchlistItems();
        setupSwipeToDelete();

        return view;
    }

    private void setupSwipeToDelete() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String session = prefs.getString("sessionId", null);

        if (session == null) {
            Toast.makeText(getContext(), "Login to remove items from watchlist.", Toast.LENGTH_SHORT).show();
            return;
        }
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                WatchlistItems removedItem = items.get(position);
                long movieId = removedItem.getId();

                SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                String session = prefs.getString("sessionId", null);

                if (session == null) {
                    Toast.makeText(getContext(), "You're not logged in", Toast.LENGTH_SHORT).show();
                    adapter.notifyItemChanged(position);
                    return;
                }

                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                Call<Map<String, String>> call = apiService.removeMovieFromWatchlist(watchlistId, movieId, "JSESSIONID=" + session);

                call.enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Movie removed", Toast.LENGTH_SHORT).show();
                            items.remove(position);
                            adapter.notifyItemRemoved(position);
                        } else {
                            Toast.makeText(getContext(), "Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                            adapter.notifyItemChanged(position);
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        adapter.notifyItemChanged(position);
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void fetchWatchlistItems() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        apiService.getWatchlistById(watchlistId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    items.clear();

                    List<?> watchlistItemsRaw = (List<?>) response.body().get("watchlistItems");

                    for (Object obj : watchlistItemsRaw) {
                        Movie movie = new Gson().fromJson(new Gson().toJson(obj), Movie.class);
                        items.add(new WatchlistItems(
                                movie.getId(),
                                movie.getTitle(),
                                movie.getDescription(),
                                movie.getCoverImage(),
                                false
                        ));
                    }

                    adapter.notifyDataSetChanged();

                    if (items.isEmpty()) {
                        Toast.makeText(getContext(), "No items in this watchlist.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to load watchlist items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
