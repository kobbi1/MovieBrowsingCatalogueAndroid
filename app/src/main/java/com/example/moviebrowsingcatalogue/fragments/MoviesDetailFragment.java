package com.example.moviebrowsingcatalogue.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.RetrofitClient;
import com.example.moviebrowsingcatalogue.core.Movie;
import com.example.moviebrowsingcatalogue.services.ApiService;
import com.example.moviebrowsingcatalogue.core.MovieDetailResponse;
import com.example.moviebrowsingcatalogue.core.Actor;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;

import android.util.Log;
import android.widget.Toast;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.example.moviebrowsingcatalogue.adapters.ReviewsAdapter;
import com.example.moviebrowsingcatalogue.core.Review;



public class MoviesDetailFragment extends Fragment {

    private Movie movie;
    private int movieId;  // Store the movie ID

    private View rootView;

    private ImageView moviePoster;
    private TextView movieTitle, movieDescription, movieDirector, movieReleaseYear, movieGenre, movieAverageRating;
    private TextView movieActors; // TextView to display the list of actors


    private RecyclerView reviewsRecyclerView;
    private ReviewsAdapter reviewsAdapter;
    private List<Review> reviews = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            movieId = getArguments().getInt("movie_id");  // Retrieve movie ID from bundle
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        movieTitle = rootView.findViewById(R.id.movieTitle);
        movieDescription = rootView.findViewById(R.id.movieDescription);
        movieDirector = rootView.findViewById(R.id.movieDirector);
        movieReleaseYear = rootView.findViewById(R.id.movieReleaseYear);
        movieGenre = rootView.findViewById(R.id.movieGenre);
        movieAverageRating = rootView.findViewById(R.id.averageRating);
        moviePoster = rootView.findViewById(R.id.movieCoverImage);

        reviewsRecyclerView = rootView.findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewsAdapter = new ReviewsAdapter(reviews);
        reviewsRecyclerView.setAdapter(reviewsAdapter);

        if (movieId > 0) {  // Ensure valid movie ID before fetching
            fetchMovieDetails();
            fetchMovieReviews();
        }

        return rootView;
    }


    private void fetchMovieDetails() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        // Call to get movie details by ID
        Call<MovieDetailResponse> call = apiService.getMovieDetails(movieId);  // Use movieId to fetch details

        call.enqueue(new Callback<MovieDetailResponse>() {
            @Override
            public void onResponse(Call<MovieDetailResponse> call, Response<MovieDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieDetailResponse movieDetailResponse = response.body();
                    movie = movieDetailResponse.getMovie();  // Extract the movie object with detailed information

                    // If the average rating is inside MovieDetailResponse, set it directly
                    if (movieDetailResponse.getAverageRating() != 0) {
                        BigDecimal avg = new BigDecimal(String.valueOf(movieDetailResponse.getAverageRating())).setScale(1, BigDecimal.ROUND_FLOOR);
                        movieAverageRating.setText("Average Rating: " + avg );
                    } else {
                        System.out.println("nei");
                        movieAverageRating.setText("Average Rating: N/A");
                    }

                    // Update the UI with the movie details


                    updateUIMovie(movie);
                } else {
                    // Handle API error (e.g., no data found)
                    showToast("Failed to load movie details.");
                }
            }

            @Override
            public void onFailure(Call<MovieDetailResponse> call, Throwable t) {
                // Handle failure (network error, etc.)
                showToast("Error fetching movie details.");
            }
        });
    }


    private void fetchMovieReviews() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        Call<List<Review>> call = apiService.getMovieReviews(movieId);

        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviews.clear();
                    reviews.addAll(response.body());  // Ensure you are adding reviews to the list
                    updateUIReview(reviews);
                } else {
                    Log.e("MoviesDetailFragment", "Failed to fetch reviews: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Log.e("MoviesDetailFragment", "Error fetching reviews", t);
            }
        });
    }

    private void updateUIMovie(Movie movie) {
        // Populate UI with the fetched movie details
        if (movie != null) {
            movieTitle.setText(movie.getTitle());
            movieDescription.setText(movie.getDescription());
            movieDirector.setText("Director: " + movie.getDirector());
            movieReleaseYear.setText("Release Year: " + movie.getReleaseYear());
            movieGenre.setText("Genre: " + movie.getGenre());

            // Load poster image
            Glide.with(getContext()).load(movie.getCoverImage()).into(moviePoster);

            // Handle actors (if any)
            LinearLayout actorsContainer = rootView.findViewById(R.id.actorsContainer);
            actorsContainer.removeAllViews();

            for (Actor actor : movie.getCast()) {
                View actorView = LayoutInflater.from(getContext()).inflate(R.layout.item_actor, actorsContainer, false);
                ShapeableImageView actorImage = actorView.findViewById(R.id.actorImage);
                TextView actorName = actorView.findViewById(R.id.actorName);

                // Load actor image using Glide
                Glide.with(getContext()).load(actor.getImage()).into(actorImage);
                actorName.setText(actor.getName());

                actorsContainer.addView(actorView);  // Add the actor view to the container
            }
        }
    }

    private void updateUIReview(List<Review> reviews) {
        // Check if reviews are available
        if (reviews != null && !reviews.isEmpty()) {
            // Clear existing reviews (if any)
            reviewsAdapter.setReviews(reviews);  // Assuming you have a method to update the list in your adapter
            reviewsAdapter.notifyDataSetChanged();
        } else {
            showToast("No reviews available for this movie.");
        }
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public static MoviesDetailFragment newInstance(int movieId) {
        MoviesDetailFragment fragment = new MoviesDetailFragment();
        Bundle args = new Bundle();
        args.putInt("movie_id", movieId);  // Pass only movie ID
        fragment.setArguments(args);
        return fragment;
    }


}

