package com.example.moviebrowsingcatalogue.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.moviebrowsingcatalogue.services.ApiService;
import com.example.moviebrowsingcatalogue.RetrofitClient;

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

            if (sessionId.isEmpty()) {
                Toast.makeText(getActivity(), "Session ID missing! Login required.", Toast.LENGTH_LONG).show();
                return;
            }

            createWatchlist(watchlist, sessionId);
        });


        return view;
    }

    private void createWatchlist(Watchlist watchlist, String sessionId) {
        Call<Map<String, Object>> call = apiService.createWatchlist(watchlist, sessionId);

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getActivity(), "Watchlist created successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getActivity(), "Failed: " + response.code(), Toast.LENGTH_LONG).show();
                    System.out.println("ERROR: " + response.errorBody().toString()); // Print error details
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace(); // Print error logs
            }
        });

    }
}
