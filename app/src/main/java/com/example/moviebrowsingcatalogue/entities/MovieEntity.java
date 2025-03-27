package com.example.moviebrowsingcatalogue.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class MovieEntity {
    @PrimaryKey
    public long id;

    @NonNull
    public String title;

    public String description;

    //TODO add more options for the local storage
}
