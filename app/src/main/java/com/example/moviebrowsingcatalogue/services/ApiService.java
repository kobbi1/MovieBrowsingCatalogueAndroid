package com.example.moviebrowsingcatalogue.services;

import com.example.moviebrowsingcatalogue.core.Movie;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("movies")
    Call<List<Movie>> getMovies();

    @GET("tvshows")
    Call<List<Movie>> getTvShows();
}
