package com.example.moviebrowsingcatalogue.storage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.moviebrowsingcatalogue.entities.MovieEntity;
import com.example.moviebrowsingcatalogue.entities.WatchlistEntity;
import com.example.moviebrowsingcatalogue.entities.WatchlistItemEntity;
import com.example.moviebrowsingcatalogue.entities.WatchlistWithMovies;

import java.util.List;

@Dao
public interface WatchlistStorage {

    // Insert watchlists
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWatchlists(List<WatchlistEntity> watchlists);

    // Insert movies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<MovieEntity> movies);

    // Insert watchlist-movie pairs (join table)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWatchlistItems(List<WatchlistItemEntity> items);

    // Get all watchlists with their associated movies
    @Transaction
    @Query("SELECT * FROM watchlists")
    List<WatchlistWithMovies> getAllWatchlistsWithMovies();

    // Clear everything if needed
    @Query("DELETE FROM watchlist_items")
    void clearWatchlistItems();

    @Query("DELETE FROM watchlists")
    void clearWatchlists();

    @Query("DELETE FROM movies")
    void clearMovies();

    @Query("DELETE FROM watchlists WHERE id = :watchlistId")
    void deleteWatchlistById(long watchlistId);

    // Delete all items associated with a specific watchlist
    @Query("DELETE FROM watchlist_items WHERE watchlistId = :watchlistId")
    void deleteWatchlistItemsByWatchlistId(long watchlistId);

    @Transaction
    default void  clearAll() {
        clearWatchlistItems();
        clearWatchlists();
        clearMovies();
    }
}
