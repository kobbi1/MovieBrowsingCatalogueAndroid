package com.example.moviebrowsingcatalogue.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class WatchlistWithMovies {

    @Embedded
    public WatchlistEntity watchlist;

    @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(
                    value = WatchlistItemEntity.class,
                    parentColumn = "watchlistId",
                    entityColumn = "movieId"
            )
    )
    public List<MovieEntity> movies;
}
