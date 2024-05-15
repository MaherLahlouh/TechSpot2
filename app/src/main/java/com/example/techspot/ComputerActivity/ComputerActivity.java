package com.example.techspot.ComputerActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techspot.Adapters.ComputerAdapter;
import com.example.techspot.BaseActivity.BaseActivity;
import com.example.techspot.ComputerDetails.ComputerDetailsActivity;
import com.example.techspot.Interfaces.MainRecyclerViewInterFace;
import com.example.techspot.Models.ComputerModel;
import com.example.techspot.Models.PhonesModel;
import com.example.techspot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ComputerActivity extends BaseActivity implements MainRecyclerViewInterFace {

    RecyclerView computerRecyclerView;
    private ComputerAdapter computerAdapter;
    private CheckBox samsungCheckBox, ddr4CheckBox, kingstonCheckBox,amdCheckBox,nvidiaCheckBox,intelCheckBox
            ,corsairCheckBox,lgCheckBox;
    private EditText minPrice, maxPrice;
    Button filterBtn;
    DatabaseReference reference;
    String imageDB;

    private List<String> selectedItem = new ArrayList<>();

    private List<ComputerModel> computerModels = new ArrayList<>();
    private List<ComputerModel> filteredComputer = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer);

        // Initialization
        computerRecyclerView = findViewById(R.id.computerRecyclerView);
        //------------------------------------------------------------
        samsungCheckBox = findViewById(R.id.samsungCheckBox);
        ddr4CheckBox = findViewById(R.id.ddr4CheckBox);
        kingstonCheckBox = findViewById(R.id.kingstonCheckBox);
        amdCheckBox = findViewById(R.id.amdCheckBox);
        nvidiaCheckBox = findViewById(R.id.nvidiaCheckBox);
        intelCheckBox = findViewById(R.id.intelCheckBox);
        corsairCheckBox = findViewById(R.id.corsairCheckBox);
        lgCheckBox = findViewById(R.id.lgCheckBox);
        minPrice = findViewById(R.id.minPrice);
        maxPrice = findViewById(R.id.maxPrice);
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");
        filterBtn = findViewById(R.id.filterButton);




        myComputerList(); // Fetch the recyclerview
        filteredComputer.addAll(computerModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        computerRecyclerView.setLayoutManager(linearLayoutManager);
        computerAdapter = new ComputerAdapter(computerModels, filteredComputer, this);
        computerRecyclerView.setAdapter(computerAdapter);

        // Check if the checkboxes are checkable or not
        setCheckBoxesAndButtonComputer();
    }

    private void myComputerList() {
        // Your computer model data initialization here
        // Modify this according to your data structure and requirements
        // Example:
        computerModels = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot items:snapshot.child("Categories").child("Computers").getChildren()){
                    String namedb = items.child("Name").getValue(String.class);
                    String brnddb = items.child("Brand").getValue(String.class);
                    String pricedb = items.child("Price").getValue(String.class);
                    for (DataSnapshot snap:items.child("Picture").getChildren()){
//                    String imageDB=items.child("Picture").child("imageURL").getValue(String.class);
                        imageDB=snap.child("imageURL").getValue(String.class);
                    }                    String Title = brnddb+" "+namedb;
                    selectedItem.add(items.child("ID").getValue(String.class));
                    computerModels.add(new ComputerModel(Title,imageDB,pricedb));

                }
                updateComputerList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateComputerList() {
        // Create a list to store the brands selected for filtering
        List<String> brandToFilter = new ArrayList<>();

        // Check which brand checkboxes are checked and add them to the brandToFilter list
        if (samsungCheckBox.isChecked()) brandToFilter.add("Samsung");
        if (ddr4CheckBox.isChecked()) brandToFilter.add("DDR4");
        if (kingstonCheckBox.isChecked()) brandToFilter.add("Kingston");
        if (amdCheckBox.isChecked()) brandToFilter.add("AMD");
        if (nvidiaCheckBox.isChecked()) brandToFilter.add("Nvidia");
        if (intelCheckBox.isChecked()) brandToFilter.add("Intel");
        if (corsairCheckBox.isChecked()) brandToFilter.add("Corsair");
        if (lgCheckBox.isChecked()) brandToFilter.add("LG");
        // If no brand is selected, show all phones, else filter the phones by selected brands
        if (brandToFilter.isEmpty()) {
            filteredComputer.clear();
            filteredComputer.addAll(computerModels);
        } else {
            List<ComputerModel> filteredByBrand = new ArrayList<>();

            // Iterate through phonesModels and add phones with selected brands to filteredByBrand list
            for (ComputerModel computerModel : computerModels) {
                boolean shouldInclude = false;
                for (String brand : brandToFilter) {
                    if (computerModel.getComputerTitle().toLowerCase().contains(brand.toLowerCase())) {
                        shouldInclude = true;
                        break;
                    }
                }
                if (shouldInclude) {
                    filteredByBrand.add(computerModel);
                }
            }

            // Update filteredPhones list with phones filtered by brand
            filteredComputer.clear();
            filteredComputer.addAll(filteredByBrand);
        }

        // Update the selectedItem list according to the filtered phonesModels
        List<String> updatedSelectedItem = new ArrayList<>();
        for (ComputerModel computerModel : filteredComputer) {
            for (ComputerModel originalModel : computerModels) {
                if (computerModel.getComputerTitle().equals(originalModel.getComputerTitle())) {
                    int index = computerModels.indexOf(originalModel);
                    if (index < selectedItem.size()) {
                        updatedSelectedItem.add(selectedItem.get(index));
                    }
                    break;
                }
            }
        }
        selectedItem.clear();
        selectedItem.addAll(updatedSelectedItem);

        // Check if price filters are applied
        String minPriceText = minPrice.getText().toString();
        String maxPriceText = maxPrice.getText().toString();
        boolean isPriceFilterApplied = !TextUtils.isEmpty(minPriceText) || !TextUtils.isEmpty(maxPriceText);

        // If price filters are applied, filter the phones by price range
        if (isPriceFilterApplied) {
            try {
                int minPriceValue = TextUtils.isEmpty(minPriceText) ? Integer.MIN_VALUE : Integer.parseInt(minPriceText);
                int maxPriceValue = TextUtils.isEmpty(maxPriceText) ? Integer.MAX_VALUE : Integer.parseInt(maxPriceText);

                List<ComputerModel> filteredByPrice = new ArrayList<>();
                for (ComputerModel computerModel : filteredComputer) {
                    int phoneprice = Integer.parseInt(computerModel.getComputerPrice().replace("$", ""));
                    if (phoneprice >= minPriceValue && phoneprice <= maxPriceValue) {
                        filteredByPrice.add(computerModel);
                    }
                }

                // Update filteredPhones list with phones filtered by price
                filteredComputer.clear();
                filteredComputer.addAll(filteredByPrice);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Notify the adapter that the dataset has changed
        computerAdapter.notifyDataSetChanged();
    }

    private void setCheckBoxesAndButtonComputer() {
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateComputerList();
            }
        });

        samsungCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateComputerList();
            }
        });

        ddr4CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateComputerList();
            }
        });

        kingstonCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateComputerList();
            }
        });

        amdCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateComputerList();
            }
        });

        nvidiaCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateComputerList();
            }
        });

        intelCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateComputerList();
            }
        });

        corsairCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateComputerList();
            }
        });

        lgCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateComputerList();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(int position) {
        // Handle item click if needed
        switch (position){
            default:
                Intent intent=new Intent(ComputerActivity.this, ComputerDetailsActivity.class);
                String s = selectedItem.get(position);
                intent.putExtra("ID", s);
                startActivity(intent);
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return true;
    }
}