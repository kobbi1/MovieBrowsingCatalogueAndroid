package com.example.moviebrowsingcatalogue.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.RetrofitClient;
import com.example.moviebrowsingcatalogue.core.AuthResponse;
import com.example.moviebrowsingcatalogue.core.User;
import com.example.moviebrowsingcatalogue.services.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText;
    private Button saveButton;
    private ApiService apiService;
    private Long userId;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        saveButton = findViewById(R.id.saveButton);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        userId = getIntent().getLongExtra("userId", -1);
        String username = prefs.getString("username", "");
        String email = prefs.getString("email", "");

        usernameEditText.setText(username);
        emailEditText.setText(email);

        saveButton.setOnClickListener(v -> updateUserProfile());
    }

    private void updateUserProfile() {
        String newUsername = usernameEditText.getText().toString().trim();
        String newEmail = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(newUsername) || TextUtils.isEmpty(newEmail)) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
            return;
        }

        User updatedUser = new User(newUsername, newEmail);

        apiService.updateUserProfile(userId, updatedUser).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("username", newUsername);
                    editor.putString("email", newEmail);
                    editor.apply();

                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}