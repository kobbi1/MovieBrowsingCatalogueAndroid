package com.example.moviebrowsingcatalogue.core;

public class ReviewRequest {
    private int rating;
    private String reviewText;
    private Long movieId;
    private Long userId;

    public ReviewRequest(int rating, String reviewText, Long movieId, Long userId) {
        this.rating = rating;
        this.reviewText = reviewText;
        this.movieId = movieId;
        this.userId = userId;
    }

    // Getters and Setters
    public int getRating() { return rating; }
    public String getReviewText() { return reviewText; }
    public Long getMovieId() { return movieId; }
    public Long getUserId() { return userId; }
}
