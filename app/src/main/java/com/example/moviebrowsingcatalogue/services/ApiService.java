package com.example.moviebrowsingcatalogue.services;

import com.example.moviebrowsingcatalogue.core.Movie;
import com.example.moviebrowsingcatalogue.core.MovieDetailResponse;
import com.example.moviebrowsingcatalogue.core.ReviewRequest;
import com.example.moviebrowsingcatalogue.core.User;
import com.example.moviebrowsingcatalogue.core.AuthResponse;
import com.example.moviebrowsingcatalogue.core.PasswordChangeRequest;
import com.example.moviebrowsingcatalogue.core.UserWatchlist;
import com.example.moviebrowsingcatalogue.core.Watchlist;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Header;
import retrofit2.http.Field;
import retrofit2.http.Path;

import okhttp3.ResponseBody;
import retrofit2.http.Query;

import com.example.moviebrowsingcatalogue.core.Review;

public interface ApiService {

    @GET("movies")
    Call<List<Movie>> getMovies();

    @GET("tvshows")
    Call<List<Movie>> getTvShows();

    @GET("movies/top-movies")
    Call<List<Movie>> getTopMovies();
    @GET("movies/categories")
    Call<List<String>> getMovieCategories();
    @GET("tvshows/categories")
    Call<List<String>> getTvShowCategories();
    @GET("movies/categories/{category}")
    Call<List<Movie>> getMoviesBySpecificCategory(@Path("category") String category);
    @GET("tvshows/categories/{category}")
    Call<List<Movie>> getTvShowsBySpecificCategory(@Path("category") String category);

    @GET("movies/{id}")
    Call<MovieDetailResponse> getMovieDetails(@Path("id") long id);

    @GET("tvshows/{id}")
    Call<Movie> getTvShowById(@Path("id") long id);


    @GET("reviews/movie/{id}")
    Call<List<Review>> getMovieReviews(@Path("id") long id);


    @POST("reviews/submit")
    Call<ResponseBody> submitReview( @Body ReviewRequest reviewRequest);

    @GET("reviews/{movie_id}/{user_id}")
    Call<Review> getReviewByMovieIdAndUserId(
            @Path("movie_id") long movieId,
            @Path("user_id") long userId
    );




    // User Auth
    @POST("users/login")
    Call<AuthResponse> loginUser(
            @Body User user
    );

    @POST("users/signup")
    Call<AuthResponse> signupUser(
            @Body User user
    );

    @GET("watchlists")
    Call<List<Watchlist>> getWatchlists();
    @POST("watchlists/create")
    Call<Map<String, Object>> createWatchlist(@Body Watchlist watchlist, @Header("Cookie") String sessionId);

    @GET("user/profile")
    Call<User> getUserProfile(@Header("Authorization") String token);

    @PUT("user/profile")
    Call<AuthResponse> updateProfile(
            @Header("Authorization") String token,
            @Body User user
    );

    @PUT("users/{id}/profile")
    Call<AuthResponse> updateUserProfile(
            @Path("id") Long id,
            @Body User user
    );

    @PATCH("users/{id}/update-password")
    Call<AuthResponse> updatePassword(
            @Path("id") long userId,
            @Body PasswordChangeRequest passwordChangeRequest
    );

    @GET("/watchlists/user/{userId}")
    Call<UserWatchlist> getUserWatchlists(@Path("userId") long userId);

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") long id);

    @DELETE("watchlists/delete/{watchlistId}/user/{userId}")
    Call<Void> deleteWatchlist(@Path("watchlistId") long watchlistId, @Path("userId") long userId);


    @POST("movies/add-to-watchlist")
    Call<ResponseBody> addMovieToWatchlist(
            @Query("movieId") long movieId,
            @Query("watchlistId") long watchlistId,
            @Header("Cookie") String sessionCookie
    );


    @POST("tvshows/add-to-watchlist")
    Call<ResponseBody> addTvShowToWatchlist(
            @Query("movieId") long movieId,
            @Query("watchlistId") long watchlistId
    );

    @GET("watchlists/{watchlistId}")
    Call<Map<String, Object>> getWatchlistById(
            @Path("watchlistId") long watchlistId
    );
    @DELETE("watchlists/{watchlistId}/remove-movie/{movieId}")
    Call<Map<String, String>> removeMovieFromWatchlist(
            @Path("watchlistId") long watchlistId,
            @Path("movieId") long movieId,
            @Header("Cookie") String sessionId
    );






}
