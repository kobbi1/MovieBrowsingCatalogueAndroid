package com.example.moviebrowsingcatalogue.core;

import java.util.List;

public class MovieDetailResponse {
    private Movie movie;
    private double averageRating;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}

