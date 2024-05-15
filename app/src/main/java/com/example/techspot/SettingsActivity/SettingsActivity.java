package com.example.techspot.SettingsActivity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techspot.BaseActivity.BaseActivity;
import com.example.techspot.EditProfile.EditProfile;
import com.example.techspot.R;
import com.example.techspot.SplashScreen.SplashScreen;
import com.google.firebase.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class SettingsActivity extends BaseActivity {

    private Switch switchTheme;
    private TextView textViewVersion, userProfileName, userSettingsEmail, userSettingsAddress,
            userSettingsPhone;
    private ImageButton imgLanguage, logoutImage;
    private ImageView backArrow;
    private Button editButton;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initializeViews();
        initializeDataBase();
        clickListeners();
        displayUserData();

    }

    @Override
    public void onClick(int position) {

    }

    private void initializeDataBase() {
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");

    }

    private void initializeViews() {
        switchTheme = findViewById(R.id.switchLightAndDark);
        imgLanguage = findViewById(R.id.rightArrowButton);
        logoutImage = findViewById(R.id.imageLogoutArrow);
        textViewVersion = findViewById(R.id.textViewVersion);
        backArrow = findViewById(R.id.backArrow);
        editButton = findViewById(R.id.editButton);
        //This line retrieves the version name
        String version = "Version : " + BuildConfig.VERSION_NAME;
        //this line sets the text of a TextView to the version string obtained in the previous step.
        textViewVersion.setText(version);
        userProfileName = findViewById(R.id.userProfileName);
        userSettingsEmail = findViewById(R.id.userSettingsEmail);
        userSettingsAddress = findViewById(R.id.userSettingsAddress);
        userSettingsPhone = findViewById(R.id.userSettingsPhone);
        // to check if the language is arabic then change the back arrow image
        if (Locale.getDefault().getLanguage().equals("ar")) {
            backArrow.setImageResource(R.drawable.right_arrow);
        } else {
            backArrow.setImageResource(R.drawable.left_arrow);
        }


    }

    //Click Listeners Initialize here
    private void clickListeners() {

        imgLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });

        logoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearUserLoginState();
                logOut();

            }
        });


        switchTheme.setChecked(isNightModeEnabled());
        switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                setNightMode(isChecked);
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfileIntent = new Intent(SettingsActivity.this, EditProfile.class);
                startActivity(editProfileIntent);
            }
        });
    }

    //This function displays a dialog for users to choose between English and Arabic languages
    private void showChangeLanguageDialog() {
        final String[] listLanguages = {"English", "عربي"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);
        mBuilder.setTitle("Choose Language");
        mBuilder.setSingleChoiceItems(listLanguages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                String selectedLanguageCode = position == 0 ? "en" : "ar";
                setAppLocale(selectedLanguageCode);
                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    //This function is responsible for changing the language/locale of the application.
    public void setAppLocale(String language) {
        // Set language and restart activity
        setLocale(language);
        Intent intent = new Intent(SettingsActivity.this, SplashScreen.class);
        startActivity(intent);
        finishAffinity();
    }

    //This function handles the logout process within an Android application.
    private void logOut() {
        //creates a dialog box
        new AlertDialog.Builder(SettingsActivity.this)
                // the message that will be displayed
                .setMessage(getString(R.string.logout_message))
                .setCancelable(false)
                // if the user click yes setPositiveButton will be executed
                .setPositiveButton(R.string.yes_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //initializes an intent to transition from the SettingsActivity to the LoginActivity.
                        Intent intent = new Intent(SettingsActivity.this, SplashScreen.class);
                        //starts the LoginActivity
                        startActivity(intent);
                        //Close current activity after logout
                        finish();
                    }
                })
                // if the user click yes setNegativeButton will be executed
                .setNegativeButton(R.string.no_btn, null)
                // displays the AlertDialog on the screen,making it visible to the user for interaction.
                .show();
    }

    private void displayUserData() {
        if (mCurrentUser != null) {
            DatabaseReference userRef = reference.child("Users").child(mCurrentUser.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("username").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String address = dataSnapshot.child("Address").getValue(String.class);
                        String phoneNumber = dataSnapshot.child("Phone_number").getValue(String.class);

                        if (username != null) {
                            userProfileName.setText(username);
                        } else {
                            Log.d("SettingsActivity", "Username is null");
                        }

                        if (email != null) {
                            userSettingsEmail.setText(email);
                        } else {
                            Log.d("SettingsActivity", "Email is null");
                        }

                        if (address != null) {
                            userSettingsAddress.setText(address);
                        } else {
                            Log.d("SettingsActivity", "Address is null");
                        }

                        if (phoneNumber != null) {
                            userSettingsPhone.setText(phoneNumber);
                        } else {
                            Log.d("SettingsActivity", "Phone number is null");
                        }
                    } else {
                        Log.d("SettingsActivity", "User data not found");
                        Toast.makeText(SettingsActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("SettingsActivity", "Database error: " + databaseError.getMessage());
                    Toast.makeText(SettingsActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.d("SettingsActivity", "User not authenticated");
            Toast.makeText(SettingsActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }


}