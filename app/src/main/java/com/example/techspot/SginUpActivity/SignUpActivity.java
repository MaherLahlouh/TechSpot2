package com.example.techspot.SginUpActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.techspot.BaseActivity.BaseActivity;
import com.example.techspot.LoginActivity.LoginActivity;
import com.example.techspot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;
import java.util.regex.Pattern;


public class SignUpActivity extends BaseActivity {
    //Widget
    private EditText name,emailSingUp,passwordSingUp,passwordConfirmSingUp,phoneNumber,address;
    TextView phoneCodeText;
    private Button singUpButton;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    private Spinner countrySpinner;
    String Phone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeViews();
        clickListeners();

    }

    @Override
    public void onClick(int position) {

    }

    private void initializeViews() {
        //inflate items from the (activity_sing_up.xml)
        name=findViewById(R.id.userName);
        emailSingUp=findViewById(R.id.sginUpEmail);
        passwordSingUp=findViewById(R.id.sginUpPassword);
        passwordConfirmSingUp=findViewById(R.id.confirmSginUpPassword);
        singUpButton=findViewById(R.id.sginUpButton);
        countrySpinner=findViewById(R.id.countryCodeSpinner);
        phoneNumber=findViewById(R.id.sginUpPhone);
        phoneCodeText=findViewById(R.id.countryCodeTextView);
        address=findViewById(R.id.sginUpAddress);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");

    }
    private void clickListeners() {
        singUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedCountry=adapterView.getItemAtPosition(position).toString();
                phoneCodeText.setText(selectedCountry);
                phoneCodeText.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(SignUpActivity.this, "Please Choose Country Key Number", Toast.LENGTH_SHORT).show();
            }
        });


    }



//    private void check() {
//        String nm = name.getText().toString();
//        String email = emailSingUp.getText().toString();
//        String password = passwordSingUp.getText().toString();
//        String conpass = passwordConfirmSingUp.getText().toString();
//        String phonecode = phoneCodeText.getText().toString();
//        String phonenumber = phoneNumber.getText().toString();
//        String addr = address.getText().toString();
//        String passpattern = "^(?=.[A-Z])(?=.[a-z])(?=.\\d).$";
//        Pattern capital = Pattern.compile(passpattern);//pattern used to check the password
//        Pattern confirm = Pattern.compile(password);// used to check the confirm password
//        Pattern EmailCheck = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
//                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
//
//
//        if(!email.isEmpty()) {// check if the email is valid
//            if (!EmailCheck.matcher(email).matches()){
//                showError(emailSingUp, "Please use a valid Email");
//            }
//            reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                boolean x = true;
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(nm.isEmpty()){ //check whether the user entered a name
//                        showError(name,"Please enter your username");
//                        x = false;
//                    }else if(nm.length() < 3){
//                        showError(name,"Username should be at least 3 characters");
//                        x = false;
//                    }
//                    for(DataSnapshot uid: snapshot.child("Users").getChildren()) {
//                        String emaildb = uid.child("email").getValue(String.class);
//                        if (Objects.equals(email, emaildb)) {
//                            showError(emailSingUp, "Email already used");
//                            x = false;
//                            break;
//                        }
//                    }
//
//                    if(!password.isEmpty()) {
//                        if (!capital.matcher(password).matches() || password.length()<8) {//check password validity
//                            showError(passwordSingUp, "Should contain at least:\n 1 capital letter \n 1 lowercase letter \n 1 digit \n 8 characters");
//                            x = false;
//                        }
//
//                    }else{
//                        showError(passwordSingUp,"Please enter your password");
//                        x = false;
//                    }
//
//
//                    if(!confirm.matcher(conpass).matches()){// check if passwords match
//                        showError(passwordConfirmSingUp,"Passwords don't match");
//                        x = false;
//                    }
//
//                    if(phonecode.isEmpty() || phonenumber.isEmpty()){
//                        showError(phoneNumber,"Please Enter a phone number");
//                        x= false;
//                    }else{
//                        Phone = phonecode+phonenumber;
//                    }
//                    if(addr.isEmpty()){
//                        showError(address,"Please enter your address");
//                        x = false;
//                    }
//                    if(x){
//
//                        register(email,password,nm,addr,Phone);
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(SignUpActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        }else{
//            showError(emailSingUp, "Please enter your Email");
//        }
//
//
//    }

    private void check() {
        String nm = name.getText().toString();
        String email = emailSingUp.getText().toString().trim();
        String password = passwordSingUp.getText().toString().trim();
        String conpass = passwordConfirmSingUp.getText().toString().trim();
        String phonecode = phoneCodeText.getText().toString();
        String phonenumber = phoneNumber.getText().toString().trim();
        String addr = address.getText().toString().trim();
        String passpattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";
        Pattern capital = Pattern.compile(passpattern);
        Pattern EmailCheck = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

        if (email.isEmpty()) {
            showError(emailSingUp, "Please enter your Email");
            return;
        }
        if (!EmailCheck.matcher(email).matches()) {
            showError(emailSingUp, "Please use a valid Email");
            return;
        }

        if (nm.isEmpty()) {
            showError(name, "Please enter your username");
            return;
        }
        if (nm.length() < 3) {
            showError(name, "Username should be at least 3 characters");
            return;
        }

        if (password.isEmpty()) {
            showError(passwordSingUp, "Please enter your password");
            return;
        }
        if (!capital.matcher(password).matches() || password.length() < 8) {
            showError(passwordSingUp, "Should contain at least:\n 1 capital letter \n 1 lowercase letter \n 1 digit \n 8 characters");
            return;
        }

        if (!conpass.equals(password)) {
            showError(passwordConfirmSingUp, "Passwords don't match");
            return;
        }

        if (phonecode.isEmpty() || phonenumber.isEmpty()) {
            showError(phoneNumber, "Please Enter a phone number");
            return;
        }

        if (addr.isEmpty()) {
            showError(address, "Please enter your address");
            return;
        }

        // All validations passed, proceed with registration
        register(email, password, nm, addr, phonecode + phonenumber);
    }



    private void VerifyEmail() {
        if(auth.getCurrentUser()!=null){
            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUpActivity.this,"A verification Email has been sent to you",Toast.LENGTH_SHORT).show();

                    }else
                        Toast.makeText(SignUpActivity.this,"Sorry there have been an Error,please try again later",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

//    private void register(String email, String password,String nm,String Address,String Phone_number) {
//        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//            @Override
//            public void onSuccess(AuthResult authResult) {
//                auth.signInWithEmailAndPassword(email,password);
//                String userid = auth.getCurrentUser().getUid();
//                reference.child("Users").child(auth.getCurrentUser().getUid()).child("username").setValue(nm);
//                reference.child("Users").child(auth.getCurrentUser().getUid()).child("email").setValue(email);
//                reference.child("Users").child(auth.getCurrentUser().getUid()).child("password").setValue(password);
//                reference.child("Users").child(auth.getCurrentUser().getUid()).child("Phone_number").setValue(Phone_number);
//                reference.child("Users").child(auth.getCurrentUser().getUid()).child("Address").setValue(Address);
//                Toast.makeText(SignUpActivity.this,"Account Created Successfully",Toast.LENGTH_SHORT).show();
//                VerifyEmail();
//                auth.signOut();
//                Intent intent=new Intent(SignUpActivity.this, LoginActivity.class);
//                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this).toBundle();
//                startActivity(intent, bundle);
//            }
//        });
//    }

    private void register(String email, String password, String nm, String Address, String Phone_number) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String userid = auth.getCurrentUser().getUid();
                reference.child("Users").child(userid).child("username").setValue(nm);
                reference.child("Users").child(userid).child("email").setValue(email);
                reference.child("Users").child(userid).child("password").setValue(password);
                reference.child("Users").child(userid).child("Phone_number").setValue(Phone_number);
                reference.child("Users").child(userid).child("Address").setValue(Address);
                Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                VerifyEmail();

                // Finish this activity and go back to login activity
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    private void showError(EditText cred, String err) { // shows the error type
        cred.setError(err);
        cred.requestFocus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}

