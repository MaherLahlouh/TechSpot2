package com.example.techspot.LoginActivity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.techspot.AdminActivity.adminActivity;
import com.example.techspot.BaseActivity.BaseActivity;
import com.example.techspot.ForgotPassword.ForgotPassword;
import com.example.techspot.MainActivity;
import com.example.techspot.R;
import com.example.techspot.SginUpActivity.SignUpActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends BaseActivity {
    private static final int RC_SIGN_IN = 2;
    // Widget
    private EditText emailLogin, passwordLogin;
    private Button buttonLogin;
    private TextView singUpNow, forgotPassword;
    private DatabaseReference reference;
    private FirebaseAuth user;
    private SignInButton googleSignInButton;
    private CheckBox rememberUserData;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        initializeFirebase();
        setupListeners();

        if (isUserLoggedIn()) {
            navigateToMainActivity();
        }
    }

    @Override
    public void onClick(int position) {
        // Empty method
    }

    private void initializeViews() {
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        buttonLogin = findViewById(R.id.buttonLogin);
        singUpNow = findViewById(R.id.textViewSingUp);
        forgotPassword = findViewById(R.id.forgotPassword);
        googleSignInButton = findViewById(R.id.signInButton);
        rememberUserData = findViewById(R.id.rememberUserData);
    }

    private void initializeFirebase() {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");
        user = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Ensure this matches your Firebase project settings
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setupListeners() {
        rememberUserData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    saveUserLogin(true);
                }
            }
        });

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        singUpNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToMainActivity();
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void check() {
        String email = emailLogin.getText().toString();
        String password = passwordLogin.getText().toString();
        String admin = "admin";
        if (Objects.equals(password, admin) && Objects.equals(email, admin)) {
            Intent intent = new Intent(LoginActivity.this, adminActivity.class);
            startActivity(intent);
            finish();
        } else {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean x = false;
                    for (DataSnapshot pe : snapshot.child("Users").getChildren()) {
                        String passdb = pe.child("password").getValue(String.class);
                        String emaildb = pe.child("email").getValue(String.class);
                        if (Objects.equals(email, emaildb) && Objects.equals(password, passdb)) {
                            x = true;
                            break;
                        }
                    }
                    if (!x) {
                        Toast.makeText(LoginActivity.this, "Your Email or Password is wrong", Toast.LENGTH_SHORT).show();
                        showError(emailLogin, "Your Email or Password is wrong");
                        showError(passwordLogin, "Your Email or Password is wrong");
                    } else {
                        user.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser firebaseUser = user.getCurrentUser();
                                            if (firebaseUser != null && firebaseUser.isEmailVerified()) {
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Please Verify Your Email", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LoginActivity.this, "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showError(EditText cred, String err) {
        cred.setError(err);
        cred.requestFocus();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        user.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser firebaseUser = user.getCurrentUser();
                            if (firebaseUser != null) {
                                checkIfUserExistsInDatabase(firebaseUser);
                            }
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkIfUserExistsInDatabase(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();
        DatabaseReference userRef = reference.child("Users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // User does not exist in database, add them
                    HashMap<String, String> userData = new HashMap<>();
                    userData.put("email", firebaseUser.getEmail());
                    userData.put("name", firebaseUser.getDisplayName());

                    userRef.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                navigateToMainActivity();
                            } else {
                                Toast.makeText(LoginActivity.this, "Failed to add user to database.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // User exists in database, proceed to main activity
                    navigateToMainActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database error, please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
