package com.example.moviebrowsingcatalogue.activities;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.fragments.GenresFragment;

public class GenresActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);

        setupNavigation(0, "Genres");

        loadFragment(new GenresFragment());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
