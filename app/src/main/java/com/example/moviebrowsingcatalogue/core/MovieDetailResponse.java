package com.example.moviebrowsingcatalogue.core;

import java.util.List;

public class MovieDetailResponse {

    private Movie movie;    // The movie object
    private List<Actor> actors;  // The list of actors

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public List<Actor> getActors() {
        return actors;
    }



    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }




}
