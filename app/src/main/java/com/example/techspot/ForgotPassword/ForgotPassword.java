package com.example.techspot.ForgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.techspot.BaseActivity.BaseActivity;
import com.example.techspot.LoginActivity.LoginActivity;
import com.example.techspot.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class ForgotPassword extends BaseActivity {

    private EditText emailForgotPassword;
    private Button forgotPasswordButton;
    private ImageView backArrowImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Initializes views for email input and button required for initiating the password reset process.
        initializeViews();
        //Sets up a click listener on the "Send Code" button.
        setUpClickListeners();
    }

    @Override
    public void onClick(int position) {

    }

    private void initializeViews() {
        emailForgotPassword = findViewById(R.id.forgotPasswordAddress);
        forgotPasswordButton = findViewById(R.id.sendCodeBtn);
        backArrowImage=findViewById(R.id.backArrowImage);
        // to check if the language is arabic then change the back arrow image
        if (Locale.getDefault().getLanguage().equals("ar")){
            backArrowImage.setImageResource(R.drawable.right_arrow);
        }else{
            backArrowImage.setImageResource(R.drawable.left_arrow);
        }
    }

    private void setUpClickListeners() {
        //Click Listener For The Button
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This String retrieves the text entered by the user in an email input field.
                String email = emailForgotPassword.getText().toString().trim();
                //Check if the String is empty it will send the message to the user
                if (email.isEmpty()) {
                    Toast.makeText(ForgotPassword.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        //Registers a listener to be called when the password reset email sending process is complete.
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                //Displaying message to the user if the task is success.
                                Toast.makeText(ForgotPassword.this, "Password reset email sent to " + email, Toast.LENGTH_SHORT).show();
                                // intent to navigate to the login screen upon successful password reset email sending.
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                //sends the email address to the login screen so that when the login screen opens, the email field is already filled in with the user's email address.
                                intent.putExtra("Email", email);
                                startActivity(intent);// Starts the LoginActivity, redirecting the user to the login screen.
                            } else {
                                ////Displaying message to the user if the task is failed.
                                Toast.makeText(ForgotPassword.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}