package com.example.moviebrowsingcatalogue.core;

public class WatchlistItems {

    private long id; // ID of the movie or TV show
    private String title;
    private String description;
    private String coverImage;
    private boolean isTvShow; // true if TV show, false if movie

    public WatchlistItems(long id, String title, String description, String coverImage, boolean isTvShow) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coverImage = coverImage;
        this.isTvShow = isTvShow;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public boolean isTvShow() {
        return isTvShow;
    }
}
