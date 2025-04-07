package com.example.moviebrowsingcatalogue.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.example.moviebrowsingcatalogue.core.ReviewRequest;
import com.example.moviebrowsingcatalogue.core.User;
import com.example.moviebrowsingcatalogue.core.UserWatchlist;
import com.example.moviebrowsingcatalogue.core.Watchlist;
import com.example.moviebrowsingcatalogue.entities.MovieEntity;
import com.example.moviebrowsingcatalogue.entities.WatchlistItemEntity;
import com.example.moviebrowsingcatalogue.services.ApiService;
import com.example.moviebrowsingcatalogue.core.MovieDetailResponse;
import com.example.moviebrowsingcatalogue.core.Actor;
import com.example.moviebrowsingcatalogue.storage.MbcDatabase;
import com.example.moviebrowsingcatalogue.storage.WatchlistStorage;
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

import okhttp3.ResponseBody;






public class MoviesDetailFragment extends Fragment {

    private Movie movie;
    private int movieId;  // Store the movie ID

    private View rootView;

    private ImageView moviePoster;
    private TextView movieTitle, movieDescription, movieDirector, movieReleaseYear, movieGenre, movieAverageRating;
    private TextView movieActors; // TextView to display the list of actors

    private EditText editReviewText;
    private RatingBar ratingBar;
    private Button btnSubmitReview , addtoWatchlist;

    private TextView reviewTextHead;

    private SharedPreferences prefs;


    private RecyclerView reviewsRecyclerView;
    private ReviewsAdapter reviewsAdapter;
    private List<Review> reviews = new ArrayList<>();

    private User user;

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

        editReviewText = rootView.findViewById(R.id.editReviewText);
        ratingBar = rootView.findViewById(R.id.ratingBar);
        btnSubmitReview = rootView.findViewById(R.id.btnSubmitReview);
        reviewTextHead = rootView.findViewById(R.id.reviewTextHead);
        addtoWatchlist = rootView.findViewById(R.id.addtoWatchlist);


        btnSubmitReview.setOnClickListener(v -> submitReview());
        addtoWatchlist.setOnClickListener(v -> showWatchlistPickerDialog(movieId));

        reviewsRecyclerView = rootView.findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewsAdapter = new ReviewsAdapter(reviews);
        reviewsRecyclerView.setAdapter(reviewsAdapter);


        prefs = requireActivity().getSharedPreferences("UserPrefs", requireActivity().MODE_PRIVATE);
        String username = prefs.getString("username", null);
        Long userId = prefs.getLong("userId", -1);


        if (username != null) {
            btnSubmitReview.setVisibility(View.VISIBLE);
            editReviewText.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);
            reviewTextHead.setVisibility(View.VISIBLE);
            addtoWatchlist.setVisibility(View.VISIBLE);
            reviewTextHead.setText("Write a Review:");



        } else {
            btnSubmitReview.setVisibility(View.GONE);
            editReviewText.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
            addtoWatchlist.setVisibility(View.GONE);
            reviewTextHead.setText("Login to write a review!");
        }

        if (movieId > 0 ) {  // Ensure valid movie ID before fetching
            fetchMovieDetails();
            fetchMovieReviews();

        }

        if(movieId > 0 && userId > 0){
            checkUserReview(movieId, userId);
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
                    // updateUIReview(reviews);
                    reviewsAdapter.setReviews(reviews);
                    reviewsAdapter.notifyDataSetChanged();
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

    private void checkUserReview(long movieId, long userId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<Review> call = apiService.getReviewByMovieIdAndUserId(movieId, userId);

        call.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Review userReview = response.body();
                    showToast("You have already reviewed this movie.");
                    disableReviewSubmission(userReview); // 
                } else {
                    showToast("No review found, you can submit one.");
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                showToast("Error checking user review.");
            }
        });
    }

    private void disableReviewSubmission(Review userReview) {
        Button submitReviewButton = rootView.findViewById(R.id.btnSubmitReview);
        submitReviewButton.setEnabled(false);
        submitReviewButton.setText("Review Submitted"); // Optional UI update


        TextView existingReviewText = rootView.findViewById(R.id.reviewTextHead);
        TextView editReviewText = rootView.findViewById(R.id.editReviewText);
        editReviewText.setText(userReview.getReviewText());
        existingReviewText.setVisibility(View.VISIBLE);
        existingReviewText.setText("Your Review: " + userReview.getReviewText());
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

    private void submitReview() {
        String reviewText = editReviewText.getText().toString().trim();
        int rating = (int) ratingBar.getRating();

        if (reviewText.isEmpty()) {
            Toast.makeText(getContext(), "Please write a review!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Dummy user email (Replace with actual user data if available)


        prefs = requireActivity().getSharedPreferences("UserPrefs", requireActivity().MODE_PRIVATE);

        long userId = prefs.getLong("userId", -1);
        if (userId == -1) {
            Toast.makeText(getActivity(), "User ID missing. Please login.", Toast.LENGTH_SHORT).show();
            System.out.println("USER ID MISSING");
            return;
        }

        fetchUserById((int) userId);

        // Create Review object
        ReviewRequest reviewRequest = new ReviewRequest(rating, reviewText, (long) movieId, userId);

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<ResponseBody> call = apiService.submitReview(reviewRequest);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() ) {
                    Toast.makeText(getContext(), "Review submitted!", Toast.LENGTH_SHORT).show();
                    editReviewText.setText(""); // Clear input
                    ratingBar.setRating(0); // Reset rating
                    fetchMovieReviews(); // Refresh reviews
                } else {
                    Toast.makeText(getContext(), "Failed to submit review.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error submitting review.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserById(int userId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<User> call = apiService.getUserById(userId);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
                    Log.d("UserFetch", "User Name: " + user.getUsername());
                    Toast.makeText(getContext(), "User: " + user.getUsername(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("UserFetch", "Failed to fetch user: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserFetch", "Error fetching user", t);
            }
        });
    }
    private void addToWatchlist(long movieId, long watchlistId) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);

        if (sessionId == null) {
            Toast.makeText(getContext(), "You're not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        String cookieHeader = "JSESSIONID=" + sessionId; 
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class); 

        Call<ResponseBody> call = apiService.addMovieToWatchlist(movieId, watchlistId, cookieHeader); 

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Movie added to watchlist!", Toast.LENGTH_SHORT).show();

                    new Thread(() -> {
                        MbcDatabase db = MbcDatabase.getInstance(requireContext());
                        WatchlistStorage storage = db.watchlistStorage();

                        MovieEntity movieEntity = new MovieEntity();
                        movieEntity.id = movie.getId();
                        movieEntity.title = movie.getTitle();
                        movieEntity.description = movie.getDescription();

                        WatchlistItemEntity itemEntity = new WatchlistItemEntity();
                        itemEntity.watchlistId = watchlistId;
                        itemEntity.movieId = movie.getId();

                        try {
                            storage.insertMovies(List.of(movieEntity));
                            storage.insertWatchlistItems(List.of(itemEntity));
                            Log.d("LocalDB", "Movie and watchlist item saved locally.");
                        } catch (Exception e) {
                            Log.e("LocalDB", "Error saving to local DB", e);
                        }
                    }).start();
                } else {
                    Toast.makeText(getContext(), "Failed to add to watchlist. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


}



    private void showWatchlistPickerDialog(long movieId) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", requireActivity().MODE_PRIVATE);
        long userId = prefs.getLong("userId", -1);
        if (userId == -1) {
            Toast.makeText(getContext(), "User ID missing. Please login.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        apiService.getUserWatchlists(userId).enqueue(new Callback<UserWatchlist>() {
            @Override
            public void onResponse(Call<UserWatchlist> call, Response<UserWatchlist> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Watchlist> watchlists = response.body().getUserWatchlists();
                    if (watchlists == null || watchlists.isEmpty()) {
                        Toast.makeText(getContext(), "You have no watchlists.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String[] watchlistNames = new String[watchlists.size()];
                    for (int i = 0; i < watchlists.size(); i++) {
                        watchlistNames[i] = watchlists.get(i).getName();
                    }

                    new AlertDialog.Builder(requireContext())
                            .setTitle("Select Watchlist")
                            .setItems(watchlistNames, (dialog, which) -> {
                                long selectedWatchlistId = watchlists.get(which).getId();
                                addToWatchlist(movieId, selectedWatchlistId);
                            })
                            .setNegativeButton("Cancel", null)
                            .show();

                } else {
                    Toast.makeText(getContext(), "Failed to fetch watchlists.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserWatchlist> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }







}

