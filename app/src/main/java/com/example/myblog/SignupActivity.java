package com.example.myblog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;

    private Button signupButton;
    private TextView loginText;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();


        // Connect XML views
        nameInput =
                findViewById(R.id.nameInput);

        emailInput =
                findViewById(R.id.emailInput);

        passwordInput =
                findViewById(R.id.passwordInput);

        confirmPasswordInput =
                findViewById(
                        R.id.confirmPasswordInput
                );

        signupButton =
                findViewById(R.id.signupButton);

        loginText =
                findViewById(R.id.loginText);


        // ==============================
        // CREATE ACCOUNT
        // ==============================

        signupButton.setOnClickListener(view -> {

            String name =
                    nameInput
                            .getText()
                            .toString()
                            .trim();

            String email =
                    emailInput
                            .getText()
                            .toString()
                            .trim();

            String password =
                    passwordInput
                            .getText()
                            .toString()
                            .trim();

            String confirmPassword =
                    confirmPasswordInput
                            .getText()
                            .toString()
                            .trim();


            // Name validation
            if (TextUtils.isEmpty(name)) {

                nameInput.setError(
                        "Please enter your name"
                );

                nameInput.requestFocus();

                return;
            }


            // Email validation
            if (TextUtils.isEmpty(email)) {

                emailInput.setError(
                        "Please enter your email"
                );

                emailInput.requestFocus();

                return;
            }


            if (!Patterns.EMAIL_ADDRESS
                    .matcher(email)
                    .matches()) {

                emailInput.setError(
                        "Please enter a valid email"
                );

                emailInput.requestFocus();

                return;
            }


            // Password validation
            if (password.length() < 6) {

                passwordInput.setError(
                        "Password must contain at least 6 characters"
                );

                passwordInput.requestFocus();

                return;
            }


            // Confirm password
            if (!password.equals(confirmPassword)) {

                confirmPasswordInput.setError(
                        "Passwords do not match"
                );

                confirmPasswordInput.requestFocus();

                return;
            }


            // Firebase Registration
            firebaseAuth
                    .createUserWithEmailAndPassword(
                            email,
                            password
                    )
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            // Save display name to Firebase user
                            if (firebaseAuth
                                    .getCurrentUser()
                                    != null) {

                                UserProfileChangeRequest profile =
                                        new UserProfileChangeRequest
                                                .Builder()
                                                .setDisplayName(name)
                                                .build();

                                firebaseAuth
                                        .getCurrentUser()
                                        .updateProfile(profile);
                            }


                            Toast.makeText(
                                    SignupActivity.this,
                                    "Account created successfully",
                                    Toast.LENGTH_SHORT
                            ).show();


                            Intent intent =
                                    new Intent(
                                            SignupActivity.this,
                                            MainActivity.class
                                    );

                            intent.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK
                                            |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                            );

                            startActivity(intent);

                            finish();

                        } else {

                            String errorMessage =
                                    "Registration failed";

                            if (task.getException() != null) {

                                errorMessage =
                                        task
                                                .getException()
                                                .getMessage();
                            }


                            Toast.makeText(
                                    SignupActivity.this,
                                    errorMessage,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        });


        // ==============================
        // BACK TO LOGIN
        // ==============================

        loginText.setOnClickListener(view -> {

            finish();

        });
    }
}