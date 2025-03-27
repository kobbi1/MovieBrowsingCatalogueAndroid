package com.example.moviebrowsingcatalogue.storage;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.moviebrowsingcatalogue.entities.MovieEntity;
import com.example.moviebrowsingcatalogue.entities.WatchlistEntity;
import com.example.moviebrowsingcatalogue.entities.WatchlistItemEntity;

@Database(
        entities = {
                WatchlistEntity.class,
                MovieEntity.class,
                WatchlistItemEntity.class
        },
        version = 1,
        exportSchema = false
)
public abstract class MbcDatabase extends RoomDatabase {
    public abstract WatchlistStorage watchlistStorage();
}
