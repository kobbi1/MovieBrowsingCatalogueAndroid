package com.example.moviebrowsingcatalogue.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.activities.ChangePasswordActivity;
import com.example.moviebrowsingcatalogue.activities.EditProfileActivity;
import com.example.moviebrowsingcatalogue.activities.UserManagementActivity;

public class ProfileFragment extends Fragment {

    private TextView usernameTextView, emailTextView;
    private Button logoutButton, editProfileButton, changePasswordButton;
    private SharedPreferences prefs;
    private long userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameTextView = view.findViewById(R.id.usernameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        logoutButton = view.findViewById(R.id.logoutButton);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        changePasswordButton = view.findViewById(R.id.changePasswordButton);

        prefs = requireActivity().getSharedPreferences("UserPrefs", requireActivity().MODE_PRIVATE);
        userId = prefs.getLong("userId", -1);

        String username = prefs.getString("username", "Guest");
        String email = prefs.getString("email", "Not available");

        usernameTextView.setText("Username: " + username);
        emailTextView.setText("Email: " + email);

        logoutButton.setOnClickListener(v -> logoutUser());

        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        changePasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        return view;
    }

    private void logoutUser() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getActivity(), UserManagementActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}