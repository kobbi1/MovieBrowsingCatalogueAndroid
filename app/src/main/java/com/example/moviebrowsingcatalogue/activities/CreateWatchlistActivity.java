package com.example.moviebrowsingcatalogue.activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.core.Watchlist;
import com.example.moviebrowsingcatalogue.services.ApiService;
import com.example.moviebrowsingcatalogue.RetrofitClient;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateWatchlistActivity extends AppCompatActivity {

    private EditText watchlistNameEditText;
    private Button createWatchlistButton;
    private SharedPreferences prefs;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_watchlist);

        watchlistNameEditText = findViewById(R.id.watchlistNameEditText);
        createWatchlistButton = findViewById(R.id.createWatchlistButton);

        apiService = RetrofitClient.getClient().create(ApiService.class);



        createWatchlistButton.setOnClickListener(v -> {
            Toast.makeText(CreateWatchlistActivity.this, "Create Button Clicked!", Toast.LENGTH_SHORT).show(); // Debugging

            String watchlistName = watchlistNameEditText.getText().toString().trim();
            if (watchlistName.isEmpty()) {
                Toast.makeText(CreateWatchlistActivity.this, "Enter a watchlist name", Toast.LENGTH_SHORT).show();
                return;
            }

            Watchlist watchlist = new Watchlist();
            watchlist.setName(watchlistName);

            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String sessionId = prefs.getString("sessionId", ""); // Retrieve session ID

            if (sessionId.isEmpty()) {
                Toast.makeText(CreateWatchlistActivity.this, "Session ID missing! Login required.", Toast.LENGTH_LONG).show();
                return; // Stop if session ID is missing
            }

            createWatchlist(watchlist, sessionId);
        });

    }

    private void createWatchlist(Watchlist watchlist, String sessionId) {
        Toast.makeText(this, "Sending API request...", Toast.LENGTH_SHORT).show(); // Debugging
        Call<Map<String, Object>> call = apiService.createWatchlist(watchlist, sessionId);

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CreateWatchlistActivity.this, "Watchlist created successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity
                } else {
                    Toast.makeText(CreateWatchlistActivity.this, "Failed: " + response.code(), Toast.LENGTH_LONG).show();
                    System.out.println("ERROR RESPONSE: " + response.errorBody()); // Log error details
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(CreateWatchlistActivity.this, "API Call Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

}
