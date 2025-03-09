package com.example.moviebrowsingcatalogue.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.RetrofitClient;
import com.example.moviebrowsingcatalogue.adapters.MoviesAdapter;
import com.example.moviebrowsingcatalogue.core.Movie;
import com.example.moviebrowsingcatalogue.services.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MoviesFragment extends Fragment  implements MoviesAdapter.MoviesClickListener{

    private String type; // "movies" or "tvshows"
    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;

    public MoviesFragment(String type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchMovies(); // Fetch and display movies
        return view;
    }

    private void fetchMovies() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Movie>> call = type.equals("movies") ? apiService.getMovies() : apiService.getTvShows();

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieList = response.body();
                    displayMovies(movieList);
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
        movieList = movies;  // Set the movie list to the data received

        // Create a new MoviesAdapter with the movie list and the click listener
        adapter = new MoviesAdapter(movieList, this);  // Pass the fragment as the listener


        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onMovieClick(Movie movie) {
        // Pass the movie object or just the movie ID to the MoviesDetailFragment
        Bundle bundle = new Bundle();
        bundle.putInt("movie_id", movie.getId());  // Pass movie ID, or you can pass the entire object
        MoviesDetailFragment movieDetailFragment = new MoviesDetailFragment();
        movieDetailFragment.setArguments(bundle);  // Pass the data using a bundle

        // Replace current fragment with the movie detail fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, movieDetailFragment);
        transaction.addToBackStack(null); // Optional, if you want to add to the back stack for navigation
        transaction.commit();
    }


    private void openMovieDetail(Movie movie) {
        MoviesDetailFragment movieDetailsFragment = MoviesDetailFragment.newInstance(movie.getId());

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, movieDetailsFragment)
                .addToBackStack(null)
                .commit();
    }


}

