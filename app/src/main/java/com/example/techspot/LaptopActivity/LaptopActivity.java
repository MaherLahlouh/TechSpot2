package com.example.techspot.LaptopActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techspot.Adapters.LaptopAdapters;
import com.example.techspot.BaseActivity.BaseActivity;
import com.example.techspot.Interfaces.MainRecyclerViewInterFace;
import com.example.techspot.Models.LaptopModel;
import com.example.techspot.PhoneAndLaptopDetails.DetailsActivity;
import com.example.techspot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LaptopActivity extends BaseActivity implements MainRecyclerViewInterFace {

    //Widget
    private RecyclerView laptopRecyclerView; // RecyclerView

    private LaptopAdapters laptopAdapters; // Object form the laptop adapter

    private CheckBox acer,dell,apple,hp,lenovo,msi,asus,other; //checkBoxes

    private DatabaseReference reference;
    private EditText minPrice,maxPrice; // Edit text to set minimum and maximum price
    private Button filterBtn,clearButton; // Filter Button


    //List of type <LaptopModel>
    private List<LaptopModel>laptopModels=new ArrayList<>();
    private List<LaptopModel>filteredLaptop=new ArrayList<>();
    private List<String>selectedItem = new ArrayList<>();
    private String imageDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptop);

        //initialization
        laptopRecyclerView=findViewById(R.id.laptopRecyclerView);
        acer=findViewById(R.id.acerCheckBox);
        dell=findViewById(R.id.dellCheckBox);
        apple=findViewById(R.id.appleCheckBox);
        hp=findViewById(R.id.hpCheckBox);
        lenovo=findViewById(R.id.lenovoCheckBox);
        msi=findViewById(R.id.msiCheckBox);
        asus=findViewById(R.id.asusCheckBox);
        other=findViewById(R.id.otherCheckBox);
        minPrice=findViewById(R.id.minPrice);
        maxPrice=findViewById(R.id.maxPrice);
        filterBtn=findViewById(R.id.filterButton);
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");        //-----------------------------------------------------------------------------------------------------------------
        myLaptopList(); //Function declare the fetch the recyclerview
        filteredLaptop.addAll(laptopModels);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        laptopRecyclerView.setLayoutManager(linearLayoutManager);
        laptopAdapters=new LaptopAdapters(laptopModels,filteredLaptop,this);
        laptopRecyclerView.setAdapter(laptopAdapters);
        laptopAdapters.setItemClickListener(this);
        setCheckBoxesLaptop();

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLaptopsList();
            }
        });

    }

//    private void updateLaptopsList() {
//        List<String> brandToFilter = new ArrayList<>();
//
//        if (acer.isChecked()) brandToFilter.add("Acer");
//        if (dell.isChecked()) brandToFilter.add("Dell");
//        if (apple.isChecked()) brandToFilter.add("Apple");
//        if (hp.isChecked()) brandToFilter.add("HP");
//        if (lenovo.isChecked()) brandToFilter.add("Lenovo");
//        if (msi.isChecked()) brandToFilter.add("MSI");
//        if (asus.isChecked()) brandToFilter.add("Asus");
//        if (other.isChecked()) brandToFilter.add("Other");
//
//        if (brandToFilter.isEmpty()) {
//            filteredLaptop.clear();
//            filteredLaptop.addAll(laptopModels);
//        } else {
//            List<LaptopModel> filteredByBrand = new ArrayList<>();
//
//            for (LaptopModel laptopModel : laptopModels) {
//                boolean shouldInclude = false;
//                for (String brand : brandToFilter) {
//                    if (laptopModel.getLaptopTitle().toLowerCase().contains(brand.toLowerCase())) {
//                        shouldInclude = true;
//                        break;
//                    }
//                }
//                if (shouldInclude) {
//                    filteredByBrand.add(laptopModel);
//                }
//            }
//
//            filteredLaptop.clear();
//            filteredLaptop.addAll(filteredByBrand);
//        }
//
//        String minPriceText = minPrice.getText().toString();
//        String maxPriceText = maxPrice.getText().toString();
//        boolean isPriceFilterApplied = !TextUtils.isEmpty(minPriceText) || !TextUtils.isEmpty(maxPriceText);
//
//        if (isPriceFilterApplied) {
//            try {
//                int minPriceValue = TextUtils.isEmpty(minPriceText) ? Integer.MIN_VALUE : Integer.parseInt(minPriceText);
//                int maxPriceValue = TextUtils.isEmpty(maxPriceText) ? Integer.MAX_VALUE : Integer.parseInt(maxPriceText);
//
//                List<LaptopModel> filteredByPrice = new ArrayList<>();
//                for (LaptopModel laptopModel : filteredLaptop) {
//                    int laptopPrice = Integer.parseInt(laptopModel.getLaptopPrice().replace("$", ""));
//                    if (laptopPrice >= minPriceValue && laptopPrice <= maxPriceValue) {
//                        filteredByPrice.add(laptopModel);
//                    }
//                }
//
//                filteredLaptop.clear();
//                filteredLaptop.addAll(filteredByPrice);
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//            }
//        }
//
//        laptopAdapters.notifyDataSetChanged();
//    }

    private void updateLaptopsList() {
        // Create a list to store the brands selected for filtering
        List<String> brandToFilter = new ArrayList<>();

        // Check which brand checkboxes are checked and add them to the brandToFilter list
        if (acer.isChecked()) brandToFilter.add("Acer");
        if (dell.isChecked()) brandToFilter.add("Dell");
        if (apple.isChecked()) brandToFilter.add("Apple");
        if (hp.isChecked()) brandToFilter.add("HP");
        if (lenovo.isChecked()) brandToFilter.add("Lenovo");
        if (msi.isChecked()) brandToFilter.add("MSI");
        if (asus.isChecked()) brandToFilter.add("Asus");
        if (other.isChecked()) brandToFilter.add("Other");

        // If no brand is selected, show all laptops, else filter the laptops by selected brands
        if (brandToFilter.isEmpty()) {
            filteredLaptop.clear();
            filteredLaptop.addAll(laptopModels);
        } else {
            List<LaptopModel> filteredByBrand = new ArrayList<>();

            // Iterate through laptopModels and add laptops with selected brands to filteredByBrand list
            for (LaptopModel laptopModel : laptopModels) {
                boolean shouldInclude = false;
                for (String brand : brandToFilter) {
                    if (laptopModel.getLaptopTitle().toLowerCase().contains(brand.toLowerCase())) {
                        shouldInclude = true;
                        break;
                    }
                }
                if (shouldInclude) {
                    filteredByBrand.add(laptopModel);
                }
            }

            // Update filteredLaptop list with laptops filtered by brand
            filteredLaptop.clear();
            filteredLaptop.addAll(filteredByBrand);
        }

        // Check if price filters are applied
        String minPriceText = minPrice.getText().toString();
        String maxPriceText = maxPrice.getText().toString();
        boolean isPriceFilterApplied = !TextUtils.isEmpty(minPriceText) || !TextUtils.isEmpty(maxPriceText);

        // If price filters are applied, filter the laptops by price range
        if (isPriceFilterApplied) {
            try {
                int minPriceValue = TextUtils.isEmpty(minPriceText) ? Integer.MIN_VALUE : Integer.parseInt(minPriceText);
                int maxPriceValue = TextUtils.isEmpty(maxPriceText) ? Integer.MAX_VALUE : Integer.parseInt(maxPriceText);

                List<LaptopModel> filteredByPrice = new ArrayList<>();
                for (LaptopModel laptopModel : filteredLaptop) {
                    int laptopPrice = Integer.parseInt(laptopModel.getLaptopPrice().replace("$", ""));
                    if (laptopPrice >= minPriceValue && laptopPrice <= maxPriceValue) {
                        filteredByPrice.add(laptopModel);
                    }
                }

                // Update filteredLaptop list with laptops filtered by price
                filteredLaptop.clear();
                filteredLaptop.addAll(filteredByPrice);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Notify the adapter that the dataset has changed
        laptopAdapters.notifyDataSetChanged();
    }


    private void myLaptopList() {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                laptopModels.clear();
                for(DataSnapshot items:snapshot.child("Categories").child("Laptops").getChildren()){
                    String namedb = items.child("Name").getValue(String.class);
                    String brnddb = items.child("Brand").getValue(String.class);
                    String pricedb = items.child("Price").getValue(String.class);
                    for (DataSnapshot snap:items.child("Picture").getChildren()){
//                    String imageDB=items.child("Picture").child("imageURL").getValue(String.class);
                        imageDB=snap.child("imageURL").getValue(String.class);
                    }
                    String Title = brnddb+" "+namedb;
                    selectedItem.add(items.child("ID").getValue(String.class));
                    laptopModels.add(new LaptopModel(Title,imageDB,pricedb));


                }

                updateLaptopsList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LaptopActivity.this,"Loading Items Failed",Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void setCheckBoxesLaptop() {
        acer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateLaptopsList();
            }
        });
        dell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateLaptopsList();
            }
        });
        apple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateLaptopsList();
            }
        });
        hp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateLaptopsList();

            }
        });
        lenovo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateLaptopsList();
            }
        });
        msi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateLaptopsList();
            }
        });
        asus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateLaptopsList();
            }
        });
        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateLaptopsList();
            }
        });


    }


    @Override
    public void onClick(int position) {
        switch (position){
            default:
                Intent intent3=new Intent(LaptopActivity.this, DetailsActivity.class);
                String s = selectedItem.get(position);
                intent3.putExtra("ID", s);
                intent3.putExtra("CAT","Laptops");
                startActivity(intent3);
                finish();
                break;
        }

    }
}