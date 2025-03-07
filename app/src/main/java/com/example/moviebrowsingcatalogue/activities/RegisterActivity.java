package com.example.moviebrowsingcatalogue.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText, emailEditText, passwordEditText;
    private Button registerButton;
    private TextView loginRedirectText;
    private ApiService apiService;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);


        registerButton.setOnClickListener(v -> registerUser());


        loginRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User(username, email, password);

        apiService.signupUser(newUser).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "Signup Successful! Please log in.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, UserManagementActivity.class)); // Redirect to Login
                    finish(); // Close RegisterActivity
                } else {
                    Toast.makeText(RegisterActivity.this, "Signup Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}