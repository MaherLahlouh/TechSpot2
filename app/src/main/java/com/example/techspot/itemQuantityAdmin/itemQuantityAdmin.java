package com.example.techspot.itemQuantityAdmin;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class itemQuantityAdmin extends AppCompatActivity {

    EditText ItemID,ItemBrand,ItemName,AddedQuantity,ItemCategory;
    Button Add;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_quantity_admin);

        ItemID = findViewById(R.id.ItemID);
        ItemBrand = findViewById(R.id.ItemBrand);
        ItemName = findViewById(R.id.ItemName);
        AddedQuantity = findViewById(R.id.AddedQuantity);
        ItemCategory = findViewById(R.id.ItemCategory);
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");        Add = findViewById(R.id.button2);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemid = ItemID.getText().toString();
                String itembrand = ItemBrand.getText().toString();
                String itemname = ItemName.getText().toString();
                String addedquantity = AddedQuantity.getText().toString();
                String itemcat = ItemCategory.getText().toString();
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DataSnapshot item= snapshot.child("Categories").child(itemcat);
                        boolean x = false;
                        int quan = Integer.parseInt(addedquantity);
                        if(item.hasChild(itemid) && !itemid.matches("")){
                            int Oldquan = item.child(itemid).child("Quantity").getValue(Integer.class);
                            int Newquan = Oldquan+quan;
                            reference.child("Categories").child(itemcat).child(itemid).child("Quantity").setValue(Newquan);
                            x = true;
                        }else{
                            for(DataSnapshot bnitem: item.getChildren()){
                                String brandb = bnitem.child("Brand").getValue(String.class);
                                String namedb = bnitem.child("Name").getValue(String.class);
                                String iddb = bnitem.child("ID").getValue(String.class);
                                if(Objects.equals(brandb,itembrand.toUpperCase()) && Objects.equals(namedb,itemname.toUpperCase())){
                                    int Oldquan = item.child(iddb).child("Quantity").getValue(Integer.class);
                                    int Newquan = Oldquan+quan;
                                    reference.child("Categories").child(itemcat).child(iddb).child("Quantity").setValue(Newquan);
                                    x = true;
                                    break;
                                }
                            }
                        }
                        if(!x){
                            showError(ItemID,"Item not in Database");
                            showError(ItemBrand,"Item not in Database");
                            showError(ItemName,"Item not in Database");
                            showError(ItemCategory,"Item not in Database");
                            Toast.makeText(itemQuantityAdmin.this,"Item not in Database",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    private void showError(EditText cred,String err) { // shows the error type
        cred.setError(err);
        cred.requestFocus();
    }
}