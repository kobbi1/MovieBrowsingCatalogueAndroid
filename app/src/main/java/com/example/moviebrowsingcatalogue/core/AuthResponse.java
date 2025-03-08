package com.example.moviebrowsingcatalogue.core;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AuthResponse implements Serializable {

        @SerializedName("message")
        private String message;

        @SerializedName("user")
        private User user;

        public String getMessage() { return message; }

        public User getUser() { return user; }

/*
    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private User user;

    public AuthResponse() {
    }

    public AuthResponse(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(User user) {
        this.user = user;
    }
 */
}