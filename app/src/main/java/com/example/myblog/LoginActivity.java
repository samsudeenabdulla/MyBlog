package com.example.myblog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;

    private Button loginButton;
    private TextView signupText;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Connect XML components
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        loginButton = findViewById(R.id.loginButton);
        signupText = findViewById(R.id.signupText);


        // ==========================
        // LOGIN BUTTON
        // ==========================

        loginButton.setOnClickListener(view -> {

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


            // Validate email
            if (TextUtils.isEmpty(email)) {

                emailInput.setError(
                        "Please enter your email"
                );

                emailInput.requestFocus();

                return;
            }


            // Validate password
            if (TextUtils.isEmpty(password)) {

                passwordInput.setError(
                        "Please enter your password"
                );

                passwordInput.requestFocus();

                return;
            }


            // Firebase Login
            firebaseAuth
                    .signInWithEmailAndPassword(
                            email,
                            password
                    )
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Login successful",
                                    Toast.LENGTH_SHORT
                            ).show();


                            Intent intent =
                                    new Intent(
                                            LoginActivity.this,
                                            MainActivity.class
                                    );

                            // Clear login screen
                            intent.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK
                                            |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                            );

                            startActivity(intent);

                            finish();

                        } else {

                            String errorMessage =
                                    "Login failed";

                            if (task.getException() != null) {

                                errorMessage =
                                        task
                                                .getException()
                                                .getMessage();
                            }


                            Toast.makeText(
                                    LoginActivity.this,
                                    errorMessage,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        });


        // ==========================
        // OPEN SIGN UP
        // ==========================

        signupText.setOnClickListener(view -> {

            Intent intent =
                    new Intent(
                            LoginActivity.this,
                            SignupActivity.class
                    );

            startActivity(intent);
        });
    }
}