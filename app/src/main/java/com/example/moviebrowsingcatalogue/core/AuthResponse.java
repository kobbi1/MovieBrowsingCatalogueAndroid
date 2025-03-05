package com.example.moviebrowsingcatalogue.core;
import com.example.moviebrowsingcatalogue.services.ApiService;

public class AuthResponse {
    private boolean success;
    private String message;
    private String token;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getToken() { return token; }
}
