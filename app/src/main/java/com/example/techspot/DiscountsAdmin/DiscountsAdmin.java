package com.example.techspot.DiscountsAdmin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techspot.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DiscountsAdmin extends AppCompatActivity {
    EditText CodeAdmin, DiscountPercent, GeneralDiscount;
    Button Add;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discounts_admin);

        //Initializes the EditText fields and Button for user input and interaction.
        initializeViews();
        //Initializes the Firebase database reference.
        initializeFirebase();
        //Sets up the click listener for the "Add" button to handle user input
        setUpClickListeners();

    }

    private void initializeViews() {
        // Find and assign EditText and Button objects from layout XML
        CodeAdmin = findViewById(R.id.CodeAdmin);
        DiscountPercent = findViewById(R.id.DiscountPercent);
        GeneralDiscount = findViewById(R.id.GeneralDiscount);
        Add = findViewById(R.id.button);
    }

    private void initializeFirebase() {
        // Get the reference to the Firebase Realtime Database
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");    }

    private void setUpClickListeners() {
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDiscount();

            }
        });

    }

    //Adds a new discount to the Firebase Realtime Database with the provided code and percentage.
    private void addDiscount(){
        // Retrieve input values from EditText fields
        String code = CodeAdmin.getText().toString();
        String discper = DiscountPercent.getText().toString();
        String general = GeneralDiscount.getText().toString();
        // Set discount percentage for the specified discount code
        reference.child("Coupons and Discounts").child(code).setValue(discper);
        // Set general discount value if provided; otherwise, set it to 0
        if (general.matches("")) {
            reference.child("Coupons and Discounts").child("General").setValue("0");
        } else {
            reference.child("Coupons and Discounts").child("General").setValue(general);
        }
    }
}