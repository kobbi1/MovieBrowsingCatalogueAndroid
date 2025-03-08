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
import com.example.moviebrowsingcatalogue.activities.RegisterActivity;
import com.example.moviebrowsingcatalogue.activities.UserManagementActivity;

public class ProfileFragment extends Fragment {

    private TextView usernameTextView, emailTextView, signUpTextView;
    private Button logoutButton, editProfileButton, loginButton;
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameTextView = view.findViewById(R.id.usernameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        logoutButton = view.findViewById(R.id.logoutButton);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        loginButton = view.findViewById(R.id.loginButton);
        signUpTextView = view.findViewById(R.id.signUpTextView);

        prefs = requireActivity().getSharedPreferences("UserPrefs", requireActivity().MODE_PRIVATE);
        String username = prefs.getString("username", null);
        String email = prefs.getString("email", null);

        // If not logged in, show Login & SignUp, Hide Edit Profile & Logout
        if (username == null) {
            usernameTextView.setVisibility(View.GONE);
            emailTextView.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
            editProfileButton.setVisibility(View.GONE);

            loginButton.setVisibility(View.VISIBLE);
            signUpTextView.setVisibility(View.VISIBLE);
        } else {
            // If logged in, show everything but hide Login & SignUp
            usernameTextView.setText("Username: " + username);
            emailTextView.setText("Email: " + email);

            usernameTextView.setVisibility(View.VISIBLE);
            emailTextView.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            editProfileButton.setVisibility(View.VISIBLE);

            loginButton.setVisibility(View.GONE);
            signUpTextView.setVisibility(View.GONE);
        }

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UserManagementActivity.class);
            startActivity(intent);
        });

        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RegisterActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> logoutUser());

        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra("userId", prefs.getLong("userId", -1));
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