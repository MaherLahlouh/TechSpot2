package com.example.techspot.ComputerDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.techspot.Adapters.CommentAdapter;
import com.example.techspot.Adapters.item_image_adapter;
import com.example.techspot.BaseActivity.BaseActivity;
import com.example.techspot.Models.CommentModel;
import com.example.techspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ComputerDetailsActivity extends BaseActivity {
    private ViewPager2 viewPager2;
    private Button minusButton,plusButton,cartButton;
    private TextView description,priceText,quantityText;

    private CommentAdapter commentAdapter;
    private RecyclerView commentRecyclerView;
    private ArrayList<CommentModel> commentmodel = new ArrayList<>();
    private item_image_adapter adapterImage;
    private List<String> images = new ArrayList<>();

    private EditText comment;
    DatabaseReference reference;
    private FirebaseAuth user;
    private int quantity=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_details);

        initializeViews();
        clickListeners();

        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");

        comment = findViewById(R.id.userCommentsEditText);
        commentRecyclerView = findViewById(R.id.recyclerViewComments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(commentmodel);
        commentRecyclerView.setAdapter(commentAdapter);

        comment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int send = comment.getRight() - comment.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    if (event.getRawX() >= send) {
                        String comm = comment.getText().toString();
                        reference.child("Comments").child(id).child(user.getCurrentUser().getUid()).setValue(comm);
                        Toast.makeText(ComputerDetailsActivity.this,"Comment Sent",Toast.LENGTH_SHORT).show();
                        comment.clearFocus();
                        return true;
                    }
                }
                return false;
            }
        });

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot detailsdb = snapshot.child("Categories").child("Computers").child(id);
                description.setText(detailsdb.child("Description").getValue(String.class));
                priceText.setText(detailsdb.child("Price").getValue(String.class));

                for(DataSnapshot imagesdb:detailsdb.child("Picture").getChildren()){
                    images.add(imagesdb.child("imageURL").getValue(String.class));
                }
                adapterImage = new item_image_adapter(ComputerDetailsActivity.this,images);
                viewPager2.setAdapter(adapterImage);
                for(DataSnapshot c:snapshot.child("Comments").child(id).getChildren()){
                    String namedb = snapshot.child("Users").child(c.getKey()).child("username").getValue(String.class);
                    String comdb = c.getValue(String.class);
                    commentmodel.add(new CommentModel(namedb,comdb));
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                // Add any custom logic if needed
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Add any custom logic if needed
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                // Add any custom logic if needed
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int quandb = snapshot.child("Categories").child("Computers").child(id).child("Quantity").getValue(Integer.class);
                        boolean f = true;

                        if(snapshot.child("Carts").child(user.getCurrentUser().getUid()).hasChild(id)){
                            if(snapshot.child("Carts").child(user.getCurrentUser().getUid()).child(id).getValue(Integer.class)+quantity<=quandb){
                                reference.child("Carts").child(user.getCurrentUser().getUid()).child(id).setValue(ServerValue.increment(quantity));
                                Toast.makeText(ComputerDetailsActivity.this,"Item Added",Toast.LENGTH_SHORT).show();
                            }else
                                Toast.makeText(ComputerDetailsActivity.this,"The quantity you desire is not available",Toast.LENGTH_SHORT).show();
                        }else if(quandb >= quantity){
                            reference.child("Carts").child(user.getCurrentUser().getUid()).child(id).setValue(quantity);
                            Toast.makeText(ComputerDetailsActivity.this,"Item Added",Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(ComputerDetailsActivity.this,"The quantity you desire is not available",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public void onClick(int position) {

    }

    private void initializeViews() {
        viewPager2=findViewById(R.id.viewPager2);
        minusButton=findViewById(R.id.minusButton);
        plusButton=findViewById(R.id.plusButton);
        cartButton=findViewById(R.id.cartButton);
        description=findViewById(R.id.descriptionText);
        priceText=findViewById(R.id.priceText);
        quantityText=findViewById(R.id.quantityText);
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");        user = FirebaseAuth.getInstance();


    }
    private void clickListeners() {
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseQuantity();
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseQuantity();
            }
        });

    }
    private void decreaseQuantity(){
        if (quantity>0){
            quantity--;
            quantityText.setText(String.valueOf(quantity));
        }
    }
    private void increaseQuantity(){
        quantity++;
        quantityText.setText(String.valueOf(quantity));
    }
}