package com.example.techspot.BaseActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Locale mCurrentLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Retrieve saved language preference from SharedPreferences
        String savedLanguage = sharedPreferences.getString("language", "");

        // Set the locale based on the saved language preference
        if (!savedLanguage.isEmpty()) {
            setLocale(savedLanguage);
        }

        // Retrieve saved theme preference from SharedPreferences
        boolean isNightMode = sharedPreferences.getBoolean("night_mode", false);
        setNightMode(isNightMode);
    }

    @   Override
    protected void onStart() {
        super.onStart();
        mCurrentLocale = getResources().getConfiguration().locale;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Locale locale = getLocale(BaseActivity.this);
        if (!locale.equals(mCurrentLocale)) {
            mCurrentLocale = locale;
            recreate();
        }
    }

    public static Locale getLocale(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String lang = sharedPreferences.getString("language", "en");
        switch (lang) {
            case "en":
                lang = "en";
                break;
            case "ar":
                lang = "ar";
                break;
        }
        return new Locale(lang);
    }

    protected void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        // Save language preference to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", language);
        editor.apply();
    }

    protected void setNightMode(boolean isNightMode) {
        AppCompatDelegate.setDefaultNightMode(isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        // Save theme preference to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("night_mode", isNightMode);
        editor.apply();
    }

    protected boolean isNightModeEnabled() {
        // Retrieve saved theme preference
        return sharedPreferences.getBoolean("night_mode", false);
    }

    protected void saveUserLogin(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("user_logged_in", isLoggedIn);
        editor.apply();
    }

    protected boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("user_logged_in", false);
    }

    protected void clearUserLoginState() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_logged_in"); // Remove the user_logged_in key
        editor.apply();
    }

    public abstract void onClick(int position);
}

