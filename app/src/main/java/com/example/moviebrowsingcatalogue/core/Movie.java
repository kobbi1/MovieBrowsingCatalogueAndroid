package com.example.moviebrowsingcatalogue.core;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Movie implements Parcelable {
    private int id;
    private String title;
    private String genre;
    private String director;
    private int releaseYear;
    private String description;
    private String type;
    private String coverImage;
    private List<Actor> cast; // Nested list of actors
    private double averageRating;  // Add averageRating field

    // Constructor for Movie
    public Movie(int id, String title, String genre, String director, int releaseYear,
                 String description, String type, String coverImage, List<Actor> cast) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.releaseYear = releaseYear;
        this.description = description;
        this.type = type;
        this.coverImage = coverImage;
        this.cast = cast;
        this.averageRating = 0.0;  // Initialize averageRating to 0.0

    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getDirector() { return director; }
    public int getReleaseYear() { return releaseYear; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getCoverImage() { return coverImage; }
    public List<Actor> getCast() { return cast; }
    public double getAverageRating() { return averageRating; }  // Getter for averageRating


    // Constructor for Parcelable
    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        genre = in.readString();
        director = in.readString();
        releaseYear = in.readInt();
        description = in.readString();
        type = in.readString();
        coverImage = in.readString();
        cast = in.createTypedArrayList(Actor.CREATOR); // Read the list of actors
        averageRating = in.readDouble();  // Read averageRating from Parcel
    }

    // Parcelable Creator
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(genre);
        dest.writeString(director);
        dest.writeInt(releaseYear);
        dest.writeString(description);
        dest.writeString(type);
        dest.writeString(coverImage);
        dest.writeTypedList(cast);  // Write the list of actors
        dest.writeDouble(averageRating);  // Write averageRating to Parcel
    }
}

