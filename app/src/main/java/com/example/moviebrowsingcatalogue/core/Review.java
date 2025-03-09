package com.example.moviebrowsingcatalogue.core;
import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("review_text")  // Map the JSON field "review_text" to the Java field "reviewText"
    private String reviewText;
    private int rating;

    private User user; // This is where the User object is stored

    // Getters
    public Review(String reviewText, User user, int rating) {
        this.reviewText = reviewText;
        this.user = user;  // Initialize user object
        this.rating = rating;
    }


    public String getReviewText() {
        return reviewText;
    }


    // Getter for user
    public User getUser() {
        return user;  // This is the method you're calling in the adapter
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

