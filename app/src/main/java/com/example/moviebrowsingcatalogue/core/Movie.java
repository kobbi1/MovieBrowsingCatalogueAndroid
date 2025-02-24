package com.example.moviebrowsingcatalogue.core;

import java.util.List;

public class Movie {
    private int id;
    private String title;
    private String genre;
    private String director;
    private int releaseYear;
    private String description;
    private String type;
    private String coverImage;
    //private List<Actor> cast; // Nested list of actors

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getDirector() { return director; }
    public int getReleaseYear() { return releaseYear; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getCoverImage() { return coverImage; }
    //public List<Actor> getCast() { return cast; }
}
