package com.example.moviebrowsingcatalogue.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.core.Watchlist;
import com.example.moviebrowsingcatalogue.entities.WatchlistEntity;
import com.example.moviebrowsingcatalogue.services.ApiService;
import com.example.moviebrowsingcatalogue.RetrofitClient;
import com.example.moviebrowsingcatalogue.storage.MbcDatabase;
import com.google.gson.Gson;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateWatchlistFragment extends Fragment {

    private EditText watchlistNameEditText;
    private Button createWatchlistButton;
    private ApiService apiService;
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_watchlist, container, false);

        watchlistNameEditText = view.findViewById(R.id.watchlistNameEditText);
        createWatchlistButton = view.findViewById(R.id.createWatchlistButton);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        prefs = requireActivity().getSharedPreferences("UserPrefs", requireActivity().MODE_PRIVATE);

        createWatchlistButton.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Create Button Clicked!", Toast.LENGTH_SHORT).show(); // Debugging

            String watchlistName = watchlistNameEditText.getText().toString().trim();
            if (watchlistName.isEmpty()) {
                Toast.makeText(getActivity(), "Enter a watchlist name", Toast.LENGTH_SHORT).show();
                return;
            }

            Watchlist watchlist = new Watchlist();
            watchlist.setName(watchlistName);

            SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", requireActivity().MODE_PRIVATE);
            String sessionId = prefs.getString("sessionId", "");
            String cookie = "JSESSIONID=" + sessionId;

            if (sessionId.isEmpty()) {
                Toast.makeText(getActivity(), "Session ID missing! Login required.", Toast.LENGTH_LONG).show();
                return;
            }

            createWatchlist(watchlist, cookie);
        });


        return view;
    }

    private void createWatchlist(Watchlist watchlist, String sessionId) {
        Call<Map<String, Object>> call = apiService.createWatchlist(watchlist, sessionId);

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> body = response.body();

                    // Extract nested watchlist data
                    Map<String, Object> watchlistMap = (Map<String, Object>) body.get("watchlist");

                    if (watchlistMap == null || watchlistMap.get("id") == null || watchlistMap.get("name") == null) {
                        Toast.makeText(getActivity(), "Invalid watchlist response", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int watchlistId = ((Double) watchlistMap.get("id")).intValue();
                    String name = (String) watchlistMap.get("name");

                    // Get userId from SharedPreferences
                    long userId = prefs.getLong("userId", -1);

                    // Save to local Room DB
                    new Thread(() -> {
                        MbcDatabase db = MbcDatabase.getInstance(requireContext());
                        WatchlistEntity entity = new WatchlistEntity();
                        entity.id = watchlistId;
                        entity.name = name;
                        entity.userId = userId;

                        db.watchlistStorage().insertWatchlists(java.util.Collections.singletonList(entity));
                    }).start();

                    Toast.makeText(getActivity(), "Watchlist created successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getActivity(), "Failed: " + response.code(), Toast.LENGTH_LONG).show();
                    System.out.println("ERROR: " + response.errorBody()); // You don't need to call .toString() here
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace(); // Log the error
            }
        });
    }

}
