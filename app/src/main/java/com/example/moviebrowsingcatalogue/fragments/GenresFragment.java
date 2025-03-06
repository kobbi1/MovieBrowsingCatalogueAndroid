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
import com.example.moviebrowsingcatalogue.activities.MoviesByGenreActivity;
import com.example.moviebrowsingcatalogue.services.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenresFragment extends Fragment {

    private LinearLayout genresContainer;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genres, container, false);
        genresContainer = view.findViewById(R.id.genresContainer);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        fetchGenres();
        return view;
    }

    private void fetchGenres() {
        fetchMovieGenres();
        fetchTvShowGenres();
    }

    private void fetchMovieGenres() {
        apiService.getMovieCategories().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayGenres(response.body(), "Movies");
                } else {
                    showToast("Movie Genres API Error: " + response.code());
                    Log.e("GenresFragment", "Movie Genres API Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                showToast("Network Error: Failed to load movie genres");
                Log.e("GenresFragment", "Movie Genres Network Error: " + t.getMessage());
            }
        });
    }

    private void fetchTvShowGenres() {
        apiService.getTvShowCategories().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayGenres(response.body(), "TV Shows");
                } else {
                    showToast("TV Show Genres API Error: " + response.code());
                    Log.e("GenresFragment", "TV Show Genres API Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                showToast("Network Error: Failed to load TV show genres");
                Log.e("GenresFragment", "TV Show Genres Network Error: " + t.getMessage());
            }
        });
    }

    private void displayGenres(List<String> genres, String type) {
        if (genres.isEmpty()) {
            showNoGenresMessage(type);
            return;
        }

        TextView sectionHeader = new TextView(requireContext());
        sectionHeader.setText(type);
        sectionHeader.setTextSize(20);
        sectionHeader.setPadding(16, 32, 16, 16);
        sectionHeader.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        genresContainer.addView(sectionHeader);

        for (String genre : genres) {
            if (genre == null || genre.trim().isEmpty()) continue;

            TextView genreTextView = new TextView(requireContext());
            genreTextView.setText(genre);
            genreTextView.setTextSize(18);
            genreTextView.setPadding(32, 24, 32, 24);
            genreTextView.setGravity(Gravity.CENTER_VERTICAL);
            genreTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));

            genreTextView.setOnClickListener(v -> openMoviesByGenre(genre, type));

            genresContainer.addView(genreTextView);

            View separator = new View(requireContext());
            separator.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
            genresContainer.addView(separator);
        }
    }

    private void openMoviesByGenre(String genre, String type) {
        if (getActivity() == null) return;
        Intent intent = new Intent(getActivity(), MoviesByGenreActivity.class);
        intent.putExtra("genre", genre);
        intent.putExtra("type", type.equals("Movies") ? "movies" : "tvshows");
        startActivity(intent);
    }

    private void showNoGenresMessage(String type) {
        TextView noGenresText = new TextView(requireContext());
        noGenresText.setText("No " + type + " genres available.");
        noGenresText.setTextSize(18);
        noGenresText.setGravity(Gravity.CENTER);
        genresContainer.addView(noGenresText);
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
