package com.example.thapp2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

@SuppressLint("CustomSplashScreen")
public class splashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        //Handler to delay transition to the main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Show buttons after splash delay
                findViewById(R.id.signInButton).setVisibility(View.VISIBLE);
                findViewById(R.id.signUpButton).setVisibility(View.VISIBLE);
            }
        }, SPLASH_TIME_OUT);

        //Button to navigate to Login Activity
        Button signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(splashScreenActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        // Button to navigate to Signup Activity
        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(splashScreenActivity.this, SignupActivity.class);
                startActivity(signupIntent);
            }
        });

    }
}