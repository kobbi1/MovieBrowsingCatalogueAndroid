package com.example.moviebrowsingcatalogue.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.RetrofitClient;
import com.example.moviebrowsingcatalogue.core.Movie;
import com.example.moviebrowsingcatalogue.services.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesFragment extends Fragment {

    private LinearLayout moviesContainer;
    private String type; // "movies" or "tvshows"

    public MoviesFragment(String type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        moviesContainer = view.findViewById(R.id.moviesContainer);

        fetchMovies();
        return view;
    }

    private void fetchMovies() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Movie>> call = type.equals("movies") ? apiService.getMovies() : apiService.getTvShows();

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayMovies(response.body());
                } else {
                    Toast.makeText(getContext(), "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("MoviesFragment", "API Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
                Log.e("MoviesFragment", "Network Error: " + t.getMessage());
            }
        });
    }

    private void displayMovies(List<Movie> movies) {
        moviesContainer.removeAllViews();

        for (Movie movie : movies) {
            View movieView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie_dynamic, moviesContainer, false);

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
