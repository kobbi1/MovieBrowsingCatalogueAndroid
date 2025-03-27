package com.example.moviebrowsingcatalogue.core;
import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("review_text")  // Map the JSON field "review_text" to the Java field "reviewText"
    private String reviewText;
    private int rating;

    private User user;

    @SerializedName("movie_id")
    private int movieId;

    // Getters
    public Review(int rating, String reviewText, int movieId, User user) {
        this.reviewText = reviewText;
        this.user = user;
        this.rating = rating;
        this.movieId = movieId;
    }


    public String getReviewText() {
        return reviewText;
    }

   public User getUser() {
        return user;
   }

    public int getRating() {
        return rating;
    }

    // Setters

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

