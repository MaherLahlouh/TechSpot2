package com.example.techspot.PhoneAndLaptopDetails;

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

public class DetailsActivity extends BaseActivity {
    private TextView os,cpu,ram,backc,frontc,display,battery,memory,gpu,price;

    private CommentAdapter commentAdapter;
    private RecyclerView commentRecyclerView;
    private ArrayList<CommentModel> commentmodel = new ArrayList<>();
    private EditText comment;
    private ViewPager2 viewPager2;
    private item_image_adapter adapterImage;
    private DatabaseReference reference;
    private FirebaseAuth user;
    private Button cartButton,clearButton;
    private List<String> images = new ArrayList<>();
    private TextView quantityText;
    private Button minusButton,plusButton;
    private int quantity=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        commentRecyclerView = findViewById(R.id.recyclerViewComments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(commentmodel);
        commentRecyclerView.setAdapter(commentAdapter);
        os = findViewById(R.id.osText);
        cpu = findViewById(R.id.cpuText);
        ram = findViewById(R.id.ramText);
        backc = findViewById(R.id.backCameraText);
        frontc = findViewById(R.id.frontCameraText);
        display = findViewById(R.id.displayText);
        battery = findViewById(R.id.batteryText);
        memory = findViewById(R.id.memoryText);
        gpu = findViewById(R.id.gpuText);
        price = findViewById(R.id.priceText);
        comment = findViewById(R.id.userCommentsEditText);
        viewPager2 = findViewById(R.id.viewPager2);
        cartButton=findViewById(R.id.cartButton);
        user = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");
        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        String path = intent.getStringExtra("CAT");
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        minusButton=findViewById(R.id.minusButton);
        plusButton=findViewById(R.id.plusButton);
        quantityText=findViewById(R.id.quantityText);
        //-----------------------------------------------------------------------------
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseQuantity();
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseQuantity();
            }
        });



//-------------------------------------------------------------------------
        comment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int send = comment.getRight() - comment.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    if (event.getRawX() >= send) {
                        String comm = comment.getText().toString();
                        reference.child("Comments").child(id).child(user.getCurrentUser().getUid()).setValue(comm);
                        Toast.makeText(DetailsActivity.this,"Comment Sent",Toast.LENGTH_SHORT).show();
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
                DataSnapshot detailsdb = snapshot.child("Categories").child(path).child(id);
                os.setText(detailsdb.child("OS").getValue(String.class));
                cpu.setText(detailsdb.child("CPU").getValue(String.class));
                ram.setText(detailsdb.child("RAM").getValue(String.class));
                backc.setText(detailsdb.child("Back Camera").getValue(String.class));
                frontc.setText(detailsdb.child("Front Camera").getValue(String.class));
                display.setText(detailsdb.child("Display").getValue(String.class));
                battery.setText(detailsdb.child("Battery").getValue(String.class));
                memory.setText(detailsdb.child("Memory").getValue(String.class));
                gpu.setText(detailsdb.child("GPU").getValue(String.class));
                price.setText(detailsdb.child("Price").getValue(String.class));
                images.add(detailsdb.child("Picture").child("imageURL").getValue(String.class));

                for(DataSnapshot imagesdb:detailsdb.child("Picture").getChildren()){
                    images.add(imagesdb.child("imageURL").getValue(String.class));
                }
                adapterImage = new item_image_adapter(DetailsActivity.this,images);
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
                        int quandb = snapshot.child("Categories").child(path).child(id).child("Quantity").getValue(Integer.class);
                        boolean f = true;

                        if(snapshot.child("Carts").child(user.getCurrentUser().getUid()).hasChild(id)){
                            if(snapshot.child("Carts").child(user.getCurrentUser().getUid()).child(id).getValue(Integer.class)+quantity<=quandb){
                                reference.child("Carts").child(user.getCurrentUser().getUid()).child(id).setValue(ServerValue.increment(quantity));
                                Toast.makeText(DetailsActivity.this,"Item Added",Toast.LENGTH_SHORT).show();
                            }else
                                Toast.makeText(DetailsActivity.this,"The quantity you desire is not available",Toast.LENGTH_SHORT).show();
                        }else if(quandb >= quantity){
                            reference.child("Carts").child(user.getCurrentUser().getUid()).child(id).setValue(quantity);
                            Toast.makeText(DetailsActivity.this,"Item Added",Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(DetailsActivity.this,"The quantity you desire is not available",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void clearData() {

    }

    @Override
    public void onClick(int position) {

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
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