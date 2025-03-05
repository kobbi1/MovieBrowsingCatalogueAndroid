package com.example.moviebrowsingcatalogue.services;

import com.example.moviebrowsingcatalogue.core.Movie;
import com.example.moviebrowsingcatalogue.core.User;
import com.example.moviebrowsingcatalogue.core.AuthResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Header;
import retrofit2.http.Field;

public interface ApiService {

    @GET("movies")
    Call<List<Movie>> getMovies();

    @GET("tvshows")
    Call<List<Movie>> getTvShows();

    @GET("movies/top-movies")
    Call<List<Movie>> getTopMovies();


    // User Authentication APIs
    @FormUrlEncoded
    @POST("auth/login")
    Call<AuthResponse> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("auth/register")
    Call<AuthResponse> registerUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("user/profile")
    Call<User> getUserProfile(@Header("Authorization") String token);

    @PUT("user/profile")
    Call<AuthResponse> updateProfile(
            @Header("Authorization") String token,
            @Body User user
    );
}