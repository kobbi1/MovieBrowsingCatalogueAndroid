package com.example.moviebrowsingcatalogue.activities;

import com.example.moviebrowsingcatalogue.core.AuthResponse;
import com.example.moviebrowsingcatalogue.services.ApiService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moviebrowsingcatalogue.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.moviebrowsingcatalogue.core.User;
import com.example.moviebrowsingcatalogue.RetrofitClient;

public class UserManagementActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText, emailEditText;
    private Button loginButton, registerButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermanagement);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        emailEditText = findViewById(R.id.emailEditText);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        loginButton.setOnClickListener(v -> loginUser());
        registerButton.setOnClickListener(v -> registerUser());
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        User loginUser = new User(username, password);

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.loginUser(loginUser).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User loggedInUser = response.body().getUser();
                    if (loggedInUser != null) {
                        String email = loggedInUser.getEmail();

                        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putLong("userId", loggedInUser.getId());
                        editor.putString("username", loggedInUser.getUsername());
                        editor.putString("email", email);
                        editor.apply();

                        Intent intent = new Intent(UserManagementActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(UserManagementActivity.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserManagementActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(UserManagementActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter username, email, and password", Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User(username, email, password);

        apiService.signupUser(newUser).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User registeredUser = response.body().getUser();
                    if (registeredUser != null) {
                        String registeredEmail = registeredUser.getEmail();


                        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("username", registeredUser.getUsername());
                        editor.putString("email", registeredEmail);
                        editor.apply();


                        Toast.makeText(UserManagementActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserManagementActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(UserManagementActivity.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserManagementActivity.this, "Signup Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(UserManagementActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}