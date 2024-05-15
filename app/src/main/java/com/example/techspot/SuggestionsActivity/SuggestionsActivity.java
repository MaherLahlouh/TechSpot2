package com.example.techspot.SuggestionsActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techspot.BaseActivity.BaseActivity;
import com.example.techspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SuggestionsActivity extends BaseActivity {

    private TextView titleSuggestion;
    private EditText fillSuggestion;
    private Button sendButton;
    private String userComment;
    private DatabaseReference reference;
    private FirebaseAuth user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        //Initializes views for the suggestion title, suggestion content, and send button.
        initializeViews();
        //Initializes Firebase Realtime Database and Firebase Authentication.
        //Obtains a reference to the Firebase Realtime Database.
        initializeFireBase();
        //Sets up a TextWatcher on the suggestion
        //content EditText (fillSuggestion) to enable/disable the send button based on whether there
        //is text input.
        clickListeners();

    }

    @Override
    public void onClick(int position) {

    }

    private void initializeViews() {
        titleSuggestion = findViewById(R.id.titleTextView);
        fillSuggestion = findViewById(R.id.suggestionEditText);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setEnabled(false);



    }
    private void initializeFireBase() {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");
        user=FirebaseAuth.getInstance();
    }
    private void clickListeners() {
        fillSuggestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendButton.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userComment = fillSuggestion.getText().toString().trim();
                if (userComment.isEmpty()) {
                    fillSuggestion.setError("Please Fill The Suggestion");
                } else {
                    Log.d("user comment sharedpref",userComment);
                    Toast.makeText(SuggestionsActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                    reference.child("Suggestions").child(user.getCurrentUser().getUid()).setValue(userComment);
                    fillSuggestion.setText("");
                }
            }
        });
    }
}