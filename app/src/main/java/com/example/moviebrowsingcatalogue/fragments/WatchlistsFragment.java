package com.example.moviebrowsingcatalogue.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.RetrofitClient;
import com.example.moviebrowsingcatalogue.core.Watchlist;
import com.example.moviebrowsingcatalogue.services.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchlistsFragment extends Fragment {

    private LinearLayout watchlistsContainer;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watchlists, container, false);
        watchlistsContainer = view.findViewById(R.id.watchlistsContainer);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        fetchWatchlists();
        return view;
    }

    private void fetchWatchlists() {
        apiService.getWatchlists().enqueue(new Callback<List<Watchlist>>() {
            @Override
            public void onResponse(@NonNull Call<List<Watchlist>> call, @NonNull Response<List<Watchlist>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayWatchlists(response.body());
                } else {
                    showToast("Watchlists API Error: " + response.code());
                    Log.e("WatchlistsFragment", "API Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Watchlist>> call, @NonNull Throwable t) {
                showToast("Network Error: Failed to load watchlists");
                Log.e("WatchlistsFragment", "Network Error: " + t.getMessage());
            }
        });
    }

    private void displayWatchlists(List<Watchlist> watchlists) {
        if (watchlists.isEmpty()) {
            showNoWatchlistsMessage();
            return;
        }

        for (Watchlist watchlist : watchlists) {
            if (watchlist == null || watchlist.getName() == null) continue;

            TextView watchlistTextView = new TextView(requireContext());
            watchlistTextView.setText(watchlist.getName());
            watchlistTextView.setTextSize(18);
            watchlistTextView.setPadding(32, 24, 32, 24);
            watchlistTextView.setGravity(Gravity.CENTER_VERTICAL);
            watchlistTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));

            watchlistTextView.setOnClickListener(v -> openWatchlistDetails(watchlist.getId()));

            watchlistsContainer.addView(watchlistTextView);

            View separator = new View(requireContext());
            separator.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
            watchlistsContainer.addView(separator);
        }
    }

    private void openWatchlistDetails(Long watchlistId) {
        if (getActivity() == null) return;
        //Intent intent = new Intent(getActivity(), WatchlistDetailsActivity.class);
        //intent.putExtra("watchlistId", watchlistId);
        //startActivity(intent);
    }

    private void showNoWatchlistsMessage() {
        TextView noWatchlistsText = new TextView(requireContext());
        noWatchlistsText.setText("No watchlists available.");
        noWatchlistsText.setTextSize(18);
        noWatchlistsText.setGravity(Gravity.CENTER);
        watchlistsContainer.addView(noWatchlistsText);
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
