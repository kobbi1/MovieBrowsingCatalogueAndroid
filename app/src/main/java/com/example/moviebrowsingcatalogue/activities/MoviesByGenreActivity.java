package com.example.moviebrowsingcatalogue.activities;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.fragments.MoviesByGenreFragment;

public class MoviesByGenreActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_by_genre);

        String genre = getIntent().getStringExtra("genre");
        String type = getIntent().getStringExtra("type"); // "movies" or "tvshows"

        setupNavigation(0, genre);

        MoviesByGenreFragment fragment = new MoviesByGenreFragment();
        Bundle args = new Bundle();
        args.putString("genre", genre);
        args.putString("type", type);
        fragment.setArguments(args);

        loadFragment(fragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
