package com.example.moviebrowsingcatalogue.core;

public class User {
    private Long id;
    private String username;
    private String email;
    private String password;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public User(String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }


    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}
