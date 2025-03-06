package com.example.moviebrowsingcatalogue.services;

import com.example.moviebrowsingcatalogue.core.Movie;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

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
}
