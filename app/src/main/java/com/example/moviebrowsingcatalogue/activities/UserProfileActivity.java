package com.example.moviebrowsingcatalogue.activities;
import com.example.moviebrowsingcatalogue.core.AuthResponse;
import com.example.moviebrowsingcatalogue.core.User;
import com.example.moviebrowsingcatalogue.services.ApiService;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.core.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {
    private EditText emailEditText, nameEditText;
    private Button updateButton, logoutButton;
    private ApiService apiService;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        emailEditText = findViewById(R.id.emailEditText);
        nameEditText = findViewById(R.id.nameEditText);
        updateButton = findViewById(R.id.updateButton);
        logoutButton = findViewById(R.id.logoutButton);

        apiService = ApiClient.getApiService();
        authToken = getSharedPreferences("AppPrefs", MODE_PRIVATE).getString("auth_token", null);

        if (authToken == null) {
            startActivity(new Intent(UserProfileActivity.this, UserManagementActivity.class));
            finish();
        }

        loadUserProfile();

        updateButton.setOnClickListener(v -> updateUserProfile());
        logoutButton.setOnClickListener(v -> logoutUser());
    }

    private void loadUserProfile() {
        apiService.getUserProfile("Bearer " + authToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    emailEditText.setText(response.body().getEmail());
                    nameEditText.setText(response.body().getName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, "Failed to load profile!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfile() {
        String updatedEmail = emailEditText.getText().toString().trim();
        String updatedName = nameEditText.getText().toString().trim();

        if (TextUtils.isEmpty(updatedEmail) || TextUtils.isEmpty(updatedName)) {
            Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        User updatedUser = new User(updatedEmail, updatedName, null);
        apiService.updateProfile("Bearer " + authToken, updatedUser).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(UserProfileActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserProfileActivity.this, "Update failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutUser() {
        getSharedPreferences("AppPrefs", MODE_PRIVATE).edit().remove("auth_token").apply();
        startActivity(new Intent(UserProfileActivity.this, UserManagementActivity.class));
        finish();
    }
}