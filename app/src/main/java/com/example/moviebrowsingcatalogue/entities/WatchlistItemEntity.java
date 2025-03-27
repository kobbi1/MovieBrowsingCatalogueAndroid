package com.example.moviebrowsingcatalogue.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "watchlist_items",
        primaryKeys = {"watchlistId", "movieId"},
        foreignKeys = {
                @ForeignKey(entity = WatchlistEntity.class,
                        parentColumns = "id",
                        childColumns = "watchlistId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = MovieEntity.class,
                        parentColumns = "id",
                        childColumns = "movieId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("watchlistId"), @Index("movieId")}
)
public class WatchlistItemEntity {
    public long watchlistId;
    public long movieId;
}
