package com.example.techspot.PaymentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.techspot.BaseActivity.BaseActivity;
import com.example.techspot.PaySuccessActivity.PaySuccessActivity;
import com.example.techspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class PaymentActivity extends BaseActivity {

    private TextView subTotal, discount, shipping, total;
    private Button cashButton;
    private DatabaseReference reference;
    private FirebaseAuth auth;

    private ImageView umniah, zain, efawateercom, jiBank, arabBank, etihadBank, safwaIslamicBank;
    private int promocode = 0;
    private String OrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initializeViews();
        initializeFireBase();
        updateCartTotal();
        clickListeners();

    }

    @Override
    public void onClick(int position) {

    }


    private void initializeViews() {
        subTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        shipping = findViewById(R.id.discountValue);
        total = findViewById(R.id.total_amount_value);
        cashButton = findViewById(R.id.cashButton);
        umniah = findViewById(R.id.umniahImageView);
        zain = findViewById(R.id.zainImageView);
        efawateercom = findViewById(R.id.efawateercomImageView);
        jiBank = findViewById(R.id.jiBank);
        arabBank = findViewById(R.id.arabBank);
        etihadBank = findViewById(R.id.etihadBank);
        safwaIslamicBank = findViewById(R.id.safwaIslamicBank);
    }

    private void initializeFireBase() {
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");
    }  //   initializeFireBase function

    private void clickListeners() {

        cashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subTotal.setText("0");
                Toast.makeText(PaymentActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PaymentActivity.this, PaySuccessActivity.class);
                startActivity(intent);
            }
        });

        umniah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://uwallet.jo/en/home/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        zain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.jo.zain.com/english/Pages/zaincashpreview.aspx/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        efawateercom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.efawateercom.jo/Portal/Home";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        jiBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.jordanislamicbank.com/ar";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

            }
        });

        arabBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.arabbank.jo/ar";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

            }
        });

        etihadBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.bankaletihad.com/en/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

            }
        });
        safwaIslamicBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.safwabank.com/en/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

            }
        });

    }


    private void updateCartTotal() {
        Intent intent = getIntent();
        String Promocode = intent.getStringExtra("PromoCode");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userid = auth.getCurrentUser().getUid();
                    float sum = 0;
                    for (DataSnapshot items : snapshot.child("Carts").child(userid).getChildren()) {
                        // Check if the item snapshot is null before accessing its child nodes
                        if (items != null && items.exists()) {
                            String cat = items.getKey().substring(0, 3);
                            String fcat;
                            int itemq = items.getValue(Integer.class);
                            if (Objects.equals(cat, "Lap")) {
                                fcat = "Laptops";
                            } else if (Objects.equals(cat, "Pho")) {
                                fcat = "Phones";
                            } else if (Objects.equals(cat, "Com")) {
                                fcat = "Computers";
                            } else {
                                fcat = "Accessories";
                            }
                            DataSnapshot priceSnapshot = snapshot.child("Categories").child(fcat).child(items.getKey()).child("Price");
                            // Check if the priceSnapshot is null before accessing its value
                            if (priceSnapshot != null && priceSnapshot.exists()) {
                                String priceString = priceSnapshot.getValue(String.class);
                                if (priceString != null) {
                                    int pricedb = Integer.parseInt(priceString);
                                    float fprice = pricedb * itemq;
                                    sum += fprice;
                                }
                            }
                        }
                    }
                    subTotal.setText(String.valueOf(sum));
                    // Rest of your onDataChange method
                } else {
                    // Handle the case where the snapshot doesn't exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}