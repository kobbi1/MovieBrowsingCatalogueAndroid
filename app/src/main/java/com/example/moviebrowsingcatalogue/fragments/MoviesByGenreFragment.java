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
import java.util.ArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.moviebrowsingcatalogue.adapters.MoviesAdapter;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesByGenreFragment extends Fragment implements MoviesAdapter.MoviesClickListener {

    private RecyclerView recyclerView;
    private MoviesAdapter movieAdapter;
    private List<Movie> movies = new ArrayList<>();
    private String genre;
    private String type;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));  // LinearLayoutManager for vertical scrolling

        movieAdapter = new MoviesAdapter(movies, this);
        recyclerView.setAdapter(movieAdapter);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        if (getArguments() != null) {
            genre = getArguments().getString("genre");
            type = getArguments().getString("type");
        }

        if (genre == null || type == null) {
            showToast("Invalid genre or type");
            return view;
        }

        fetchMoviesByGenre();
        return view;
    }

    private void fetchMoviesByGenre() {
        Call<List<Movie>> call;

        if (type.equals("movies")) {
            call = apiService.getMoviesBySpecificCategory(genre);
        } else {
            call = apiService.getTvShowsBySpecificCategory(genre);
        }

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movies.clear();  // Clear any previous data
                    movies.addAll(response.body());  // Add new movies
                    movieAdapter.notifyDataSetChanged();
                } else {
                    showToast("API Error: " + response.code());
                    Log.e("MoviesByGenreFragment", "API Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                showToast("Network Error");
                Log.e("MoviesByGenreFragment", "Network Error: " + t.getMessage());
            }
        });
    }

    @Override
    public void onMovieClick(Movie movie) {
        // When a movie is clicked, open its details page
        openMovieDetail(movie);  // Assuming openMovieDetail() is implemented to handle movie click
    }

    private void openMovieDetail(Movie movie) {
        MoviesDetailFragment movieDetailsFragment = MoviesDetailFragment.newInstance(movie.getId());

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, movieDetailsFragment)
                .addToBackStack(null)
                .commit();
    }


    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
