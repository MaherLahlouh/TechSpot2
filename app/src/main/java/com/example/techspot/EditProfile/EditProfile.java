package com.example.techspot.EditProfile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techspot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EditProfile extends AppCompatActivity {

    //Widget
    private EditText email,address,userName,phoneNumber;
    private Button submitChanges;
    //Database Reference
    private DatabaseReference reference;
    private FirebaseAuth auth;

    private String Password;
    private String oldEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //This function for initialize views with id's in the xml file
        initializeViews();
        //For Button When The User Click  It
        clickListeners();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot user = snapshot.child("Users").child(auth.getCurrentUser().getUid());
                email.setText(user.child("email").getValue(String.class));
                address.setText(user.child("Address").getValue(String.class));
                userName.setText(user.child("username").getValue(String.class));
                phoneNumber.setText(user.child("Phone_number").getValue(String.class));
                Password = user.child("password").getValue(String.class);
                oldEmail = user.child("email").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    private void initializeViews() {
        userName=findViewById(R.id.userNameEditText);
        email=findViewById(R.id.emailEditTxt);
        phoneNumber=findViewById(R.id.phoneEditTxt);
        address=findViewById(R.id.addressEditTxt);
        submitChanges=findViewById(R.id.submitChanges);
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");
        auth = FirebaseAuth.getInstance();

    }

    private void clickListeners() {
        submitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userName.getText().toString();
                String Email = email.getText().toString();
                String phonenum = phoneNumber.getText().toString();
                String Address = address.getText().toString();


                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot emailsdb:snapshot.child("Users").getChildren()){
                            if(Objects.equals(emailsdb.child("email").getValue(String.class),Email) || Email.isEmpty()){
                                showError(email,"Email already used");
                                Toast.makeText(EditProfile.this,"Email already used",Toast.LENGTH_SHORT).show();
                            }else{

                                Objects.requireNonNull(auth.getCurrentUser()).verifyBeforeUpdateEmail(Email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        reference.child("Users").child(auth.getCurrentUser().getUid()).child("email").setValue(Email);
                                    }
                                });

                                Toast.makeText(EditProfile.this,"A Verification Email has been sent to the new Email",Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(!username.isEmpty()){
                            reference.child("Users").child(auth.getCurrentUser().getUid()).child("username").setValue(username);
                        }
                        if(!phonenum.isEmpty()){
                            reference.child("Users").child(auth.getCurrentUser().getUid()).child("Phone_number").setValue(phonenum);
                        }
                        if(!Address.isEmpty()) {
                            reference.child("Users").child(auth.getCurrentUser().getUid()).child("Address").setValue(Address);
                        }
                        Toast.makeText(EditProfile.this, "Changes Submitted", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
    private void showError(EditText cred, String err) { // shows the error type
        cred.setError(err);
        cred.requestFocus();
    }
}