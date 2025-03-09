package com.example.moviebrowsingcatalogue.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.fragments.MoviesFragment;
import com.example.moviebrowsingcatalogue.core.Movie;
import java.util.List;
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private MoviesClickListener listener;  // Using the MoviesClickListener interface here

    public MoviesAdapter(List<Movie> movies, MoviesClickListener listener) {
        this.movies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_dynamic, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.titleTextView.setText(movie.getTitle());
        holder.genreTextView.setText("Genre: " + movie.getGenre());
        holder.directorTextView.setText("Director: " + movie.getDirector());

        Glide.with(holder.itemView.getContext())
                .load(movie.getCoverImage())
                .into(holder.coverImageView);

        holder.itemView.setOnClickListener(v -> listener.onMovieClick(movie));  // Handle click using the listener
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, genreTextView, directorTextView;
        ImageView coverImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            genreTextView = itemView.findViewById(R.id.genreTextView);
            directorTextView = itemView.findViewById(R.id.directorTextView);
            coverImageView = itemView.findViewById(R.id.coverImageView);
        }
    }

    // Interface for handling movie click events
    public interface MoviesClickListener {
        void onMovieClick(Movie movie);  // Method to handle movie click event
    }
}
