package com.example.moviebrowsingcatalogue.activities;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.fragments.ProfileFragment;

public class ProfileActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupNavigation(R.id.nav_profile, "Profile");
        loadFragment(new ProfileFragment());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
