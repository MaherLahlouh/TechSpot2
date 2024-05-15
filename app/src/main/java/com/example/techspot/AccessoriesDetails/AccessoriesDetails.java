package com.example.techspot.AccessoriesDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class AccessoriesDetails extends BaseActivity {
    private ViewPager2 viewPager2;
    private Button firstColor,secondColor,thirdColor,fourthColor,fifthColor;
    private TextView textItemName,compatibleText,materialText,factorText,screenSizeText, specialFeatureText
            ,chargerText,cablesText,batteryCapacityText,priceText,quantityText;
    private EditText comment;
    private RecyclerView commentRecyclerView;
    private Button cartButton,minusButton,plusButton;
    // Adapter for displaying comments in a RecyclerView.
    private CommentAdapter commentAdapter;
    private item_image_adapter adapterImage;
    private List<String> images = new ArrayList<>();
    // List of comment models to be displayed in the RecyclerView.
    private ArrayList<CommentModel> commentmodel = new ArrayList<>();

    //// Represents the quantity of the item selected by the user.
    private int quantity=0;
    private DatabaseReference reference;
    private FirebaseAuth user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessories_details);

        //Initialize views and set click listeners
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
                        Toast.makeText(AccessoriesDetails.this,"Comment Sent",Toast.LENGTH_SHORT).show();
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
                DataSnapshot detailsdb = snapshot.child("Categories").child("Accessories").child(id);
                textItemName.setText(detailsdb.child("Name").getValue(String.class));
                compatibleText.setText(detailsdb.child("Compatible Devices").getValue(String.class));
                materialText.setText(detailsdb.child("Material").getValue(String.class));
                factorText.setText(detailsdb.child("Factor").getValue(String.class));
                screenSizeText.setText(detailsdb.child("Screen Size").getValue(String.class));
                specialFeatureText.setText(detailsdb.child("Special Features").getValue(String.class));
                chargerText.setText(detailsdb.child("Charger").getValue(String.class));
                cablesText.setText(detailsdb.child("Cables").getValue(String.class));
                batteryCapacityText.setText(detailsdb.child("Battery Capacity").getValue(String.class));

                priceText.setText(detailsdb.child("Price").getValue(String.class));

                for(DataSnapshot imagesdb:detailsdb.child("Picture").getChildren()){
                    images.add(imagesdb.child("imageURL").getValue(String.class));
                }
                adapterImage = new item_image_adapter(AccessoriesDetails.this,images);
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
                        int quandb = snapshot.child("Categories").child("Accessories").child(id).child("Quantity").getValue(Integer.class);
                        boolean f = true;

                        if(snapshot.child("Carts").child(user.getCurrentUser().getUid()).hasChild(id)){
                            if(snapshot.child("Carts").child(user.getCurrentUser().getUid()).child(id).getValue(Integer.class)+quantity<=quandb){
                                reference.child("Carts").child(user.getCurrentUser().getUid()).child(id).setValue(ServerValue.increment(quantity));
                                Toast.makeText(AccessoriesDetails.this,"Item Added",Toast.LENGTH_SHORT).show();
                            }else
                                Toast.makeText(AccessoriesDetails.this,"The quantity you desire is not available",Toast.LENGTH_SHORT).show();
                        }else if(quandb >= quantity){
                            reference.child("Carts").child(user.getCurrentUser().getUid()).child(id).setValue(quantity);
                            Toast.makeText(AccessoriesDetails.this,"Item Added",Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(AccessoriesDetails.this,"The quantity you desire is not available",Toast.LENGTH_SHORT).show();
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
        firstColor=findViewById(R.id.firstColorButton);
        secondColor=findViewById(R.id.secondColorButton);
        thirdColor=findViewById(R.id.thirdColorButton);
        fourthColor=findViewById(R.id.fourthColorButton);
        fifthColor=findViewById(R.id.fifthColorButton);
        textItemName=findViewById(R.id.textItemName);
        compatibleText=findViewById(R.id.compatibleText);
        materialText=findViewById(R.id.materialText);
        factorText=findViewById(R.id.factorText);
        screenSizeText=findViewById(R.id.screenSizeText);
        specialFeatureText=findViewById(R.id.specialFeatureText);
        chargerText=findViewById(R.id.chargerText);
        cablesText=findViewById(R.id.cablesText);
        batteryCapacityText=findViewById(R.id.batteryCapacityText);
        priceText=findViewById(R.id.priceText);
        quantityText=findViewById(R.id.quantityText);
        comment=findViewById(R.id.userCommentsEditText);
        commentRecyclerView=findViewById(R.id.recyclerViewComments);
        cartButton=findViewById(R.id.cartButton);
        minusButton=findViewById(R.id.minusButton);
        plusButton=findViewById(R.id.plusButton);
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");
        user = FirebaseAuth.getInstance();

    }
    //Sets click listeners for buttons in the activity.
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

        firstColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AccessoriesDetails.this, "click", Toast.LENGTH_SHORT).show();
                //viewPager2.setCurrentItem(0);  // to set a specific image
            }
        });

    }

    //Decreases the quantity of the item.
    private void decreaseQuantity(){
        if (quantity>0){
            quantity--;
            quantityText.setText(String.valueOf(quantity));
        }
    }
    //Increases the quantity of the item
    private void increaseQuantity(){
        quantity++;
        quantityText.setText(String.valueOf(quantity));
    }
}