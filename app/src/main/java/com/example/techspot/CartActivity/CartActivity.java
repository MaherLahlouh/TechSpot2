package com.example.techspot.CartActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techspot.Adapters.CartItemAdapter;
import com.example.techspot.BaseActivity.BaseActivity;
import com.example.techspot.Models.CartItemsModel;
import com.example.techspot.PaymentActivity.PaymentActivity;
import com.example.techspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CartActivity extends BaseActivity {

    private Button orderButton;
    private CartItemAdapter cartItemAdapter;
    private RecyclerView cartRecyclerView;
    private ArrayList<CartItemsModel> Cartmodel = new ArrayList<>();
    EditText Promo;
    String path;
    DatabaseReference reference;
    FirebaseAuth auth;
    String promodisc = "0";

    //Intent intent=getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        auth = FirebaseAuth.getInstance();
        cartRecyclerView=findViewById(R.id.cartRecyclerView);
        orderButton=findViewById(R.id.orderButton);
        Promo = findViewById(R.id.PromoCart);
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        cartRecyclerView.setLayoutManager(linearLayoutManager);
        cartItemAdapter = new CartItemAdapter(Cartmodel);
        cartRecyclerView.setAdapter(cartItemAdapter);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item : snapshot.child("Carts").child(auth.getCurrentUser().getUid()).getChildren()) {
                    int quandb = item.getValue(Integer.class);
                    String quan = String.valueOf(quandb) + 'x';
                    String subid = item.getKey().substring(0, 3);
                    if (Objects.equals(subid, "Lap")) {
                        path = "Laptops";
                    } else if(Objects.equals(subid, "Pho")) {
                        path = "Phones";
                    }else if(Objects.equals(subid, "Com")) {
                        path = "Computers";
                    }else if(Objects.equals(subid, "Acc")) {
                        path = "Accessories";
                    }
                    String branddb = snapshot.child("Categories").child(path).child(item.getKey()).child("Brand").getValue(String.class);
                    String namedb = snapshot.child("Categories").child(path).child(item.getKey()).child("Name").getValue(String.class);
                    String pricedb = snapshot.child("Categories").child(path).child(item.getKey()).child("Price").getValue(String.class);
                    String title = branddb + " " + namedb;
                    Cartmodel.add(new CartItemsModel(quan, title, pricedb, R.drawable.delete_item_32px,item.getKey()));

                }
                cartItemAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String promo = Promo.getText().toString();
                        if(!promo.isEmpty()&&snapshot.child("Coupons and Discounts").hasChild(promo)&&!promo.matches("General")){
                            promodisc =promo;
                        }else{
                            promodisc = "0";
                        }
                        Intent intent=new Intent(CartActivity.this, PaymentActivity.class);
                        intent.putExtra("PromoCode",promodisc);
                        startActivity(intent);

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

}
//    private void initializeViews() {
//        cartRecyclerView = findViewById(R.id.cartRecyclerView);
//        orderButton = findViewById(R.id.orderButton);
//        Promo = findViewById(R.id.PromoCart);
//    }
//    private void setupCartRecyclerView() {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        cartRecyclerView.setLayoutManager(linearLayoutManager);
//        cartItemAdapter = new CartItemAdapter(Cartmodel);
//        cartRecyclerView.setAdapter(cartItemAdapter);
//    }
//    private void fetchCartItemsFromDatabase() {
//        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");
//        auth = FirebaseAuth.getInstance();
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot item : snapshot.child("Carts").child(auth.getCurrentUser().getUid()).getChildren()) {
//                    processCartItemSnapshot(item, snapshot);
//                }
//                cartItemAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//            }
//        });
//    }
//    private void processCartItemSnapshot(DataSnapshot item, DataSnapshot snapshot) {
//        int quandb = item.getValue(Integer.class);
//        String quan = String.valueOf(quandb) + 'x';
//        String subid = item.getKey().substring(0, 3);
//        String path;
//        if (Objects.equals(subid, "Lap")) {
//            path = "Laptops";
//        } else if (Objects.equals(subid, "Pho")) {
//            path = "Phones";
//        } else if (Objects.equals(subid, "Com")) {
//            path = "Computers";
//        } else if (Objects.equals(subid, "Acc")) {
//            path = "Accessories";
//        } else {
//            return;
//        }
//        String branddb = snapshot.child("Categories").child(path).child(item.getKey()).child("Brand").getValue(String.class);
//        String namedb = snapshot.child("Categories").child(path).child(item.getKey()).child("Name").getValue(String.class);
//        String pricedb = snapshot.child("Categories").child(path).child(item.getKey()).child("Price").getValue(String.class);
//        String title = branddb + " " + namedb;
//        Cartmodel.add(new CartItemsModel(quan, title, pricedb, R.drawable.delete_item_32px, item.getKey()));
//    }
//    private void setupOrderButton() {
//        orderButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                processOrder();
//            }
//        });
//    }
//    private void processOrder() {
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String promo = Promo.getText().toString();
//                if (!promo.isEmpty() && snapshot.child("Coupons and Discounts").hasChild(promo) && !promo.matches("General")) {
//                    promodisc = promo;
//                } else {
//                    promodisc = "0";
//                }
//                startPaymentActivity();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//            }
//        });
//    }
//    private void startPaymentActivity() {
//        Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
//        intent.putExtra("PromoCode", promodisc);
//        startActivity(intent);
//    }

