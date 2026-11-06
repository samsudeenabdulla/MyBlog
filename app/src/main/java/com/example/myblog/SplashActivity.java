package com.example.myblog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 1800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {

            FirebaseAuth auth = FirebaseAuth.getInstance();

            // Check if user is already logged in
            if (auth.getCurrentUser() != null) {

                Intent intent =
                        new Intent(
                                SplashActivity.this,
                                MainActivity.class
                        );

                startActivity(intent);

            } else {

                Intent intent =
                        new Intent(
                                SplashActivity.this,
                                LoginActivity.class
                        );

                startActivity(intent);
            }

            finish();

        }, SPLASH_TIME);
    }
}