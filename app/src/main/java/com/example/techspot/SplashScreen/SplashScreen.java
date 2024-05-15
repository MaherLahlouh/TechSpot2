package com.example.techspot.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techspot.BaseActivity.BaseActivity;
import com.example.techspot.LoginActivity.LoginActivity;
import com.example.techspot.MainActivity;
import com.example.techspot.R;

public class SplashScreen extends BaseActivity {
    private ImageView splashLogo;
    private ProgressBar loadingIndicator;
    private static final int SPLASH_DURATION = 3000; //SPLASH_DURATION milliseconds.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initializeViews();
        animationCode();

    }

    @Override
    public void onClick(int position) {

    }

    //This method initializes the views used in the splash screen.
    private void initializeViews() {
        // Find the splash logo view
        splashLogo = findViewById(R.id.splash_logo);
        // Find the loading indicator view
        loadingIndicator = findViewById(R.id.loading_indicator);
        // Load animation for the logo
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        // Set the animation to the splash logo
        splashLogo.setAnimation(animation);
    }

    //This method handles the animation and transition logic of the splash screen.
    private void animationCode() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent that go to the Login Activity after the SPLASH_DURATION
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                //Start LoginActivity
                startActivity(intent);
                // Finish the current activity (SplashScreen)
                finish();
            }
        }, SPLASH_DURATION);// Delay specified duration before executing the code inside run() method
    }
}