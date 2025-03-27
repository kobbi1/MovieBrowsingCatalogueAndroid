package com.example.moviebrowsingcatalogue.storage;

import static okhttp3.internal.Internal.instance;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
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

    private static volatile MbcDatabase instance;

    public static synchronized MbcDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MbcDatabase.class, "mbc_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
