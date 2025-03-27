package com.example.moviebrowsingcatalogue.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "watchlists")
public class WatchlistEntity {
    @PrimaryKey
    public long id;

    @NonNull
    public String name;

    public long userId; // To link the watchlist to the user
}
