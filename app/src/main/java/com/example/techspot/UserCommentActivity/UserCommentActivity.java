package com.example.techspot.UserCommentActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techspot.Adapters.CommentAdapter;
import com.example.techspot.BaseActivity.BaseActivity;
import com.example.techspot.Models.CommentModel;
import com.example.techspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserCommentActivity extends BaseActivity {
    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private ArrayList<CommentModel> commentList;
    DatabaseReference reference;
    FirebaseAuth user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_comment);

        //        This method initializes the RecyclerView for displaying comments.
//        It sets up the layout manager and adapter for the RecyclerView.
        initializeViews();
//        This method initializes Firebase Realtime Database and Firebase Authentication.
//        It obtains a reference to the Firebase Realtime Database.
        initializeFireBase();
        //This method sets up a ValueEventListener on the Firebase Realtime Database reference.
        clickListeners();
    }

    @Override
    public void onClick(int position) {

    }

    private void initializeViews() {
        commentsRecyclerView = findViewById(R.id.commentRecyclerView);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);
        commentsRecyclerView.setAdapter(commentAdapter);
    }
    private void initializeFireBase() {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");        user = FirebaseAuth.getInstance();
    }
    private void clickListeners() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.child("Suggestions").getChildren()) {
                    String idDB = dataSnapshot.getKey();
                    String userNameDB = snapshot.child("Users").child(idDB).child("username").getValue(String.class);
                    String commentDB = dataSnapshot.getValue(String.class);
                    commentList.add(new CommentModel(userNameDB, commentDB));
                }
                commentAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserCommentActivity.this, "There's an error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}