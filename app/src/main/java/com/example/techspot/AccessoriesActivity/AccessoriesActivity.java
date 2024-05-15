package com.example.techspot.AccessoriesActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techspot.AccessoriesDetails.AccessoriesDetails;
import com.example.techspot.Adapters.AccessoriesAdapter;
import com.example.techspot.BaseActivity.BaseActivity;
import com.example.techspot.Interfaces.MainRecyclerViewInterFace;
import com.example.techspot.Models.AccessoriesModel;
import com.example.techspot.Models.PhonesModel;
import com.example.techspot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccessoriesActivity extends BaseActivity implements MainRecyclerViewInterFace {

    // Initialization
    RecyclerView accessoriesRecyclerView;
    private AccessoriesAdapter accessoriesAdapter;
    private CheckBox beatsCheckBox, jblCheckBox, appleCheckBox, lenovoCheckBox, googlesCheckBox, sonyCheckBox
    ,logitechCheckBox,razerCheckBox;
    private EditText minPrice, maxPrice;
    DatabaseReference reference;
    String imageDB;
    Button filterBtn;

    private List<AccessoriesModel> accessoriesModels = new ArrayList<>();
    private List<AccessoriesModel> filteredAccessories = new ArrayList<>();
    private List<String>selectedItem = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessories);



        // Initialization
        accessoriesRecyclerView = findViewById(R.id.accessoriesRecyclerView);
        beatsCheckBox = findViewById(R.id.beatsCheckBox);
        jblCheckBox = findViewById(R.id.jblCheckBox);
        appleCheckBox = findViewById(R.id.appleCheckBox);
        sonyCheckBox = findViewById(R.id.sonyCheckBox);
        lenovoCheckBox = findViewById(R.id.lenovoCheckBox);
        googlesCheckBox = findViewById(R.id.googlesCheckBox);
        logitechCheckBox = findViewById(R.id.logitechCheckBox);
        razerCheckBox = findViewById(R.id.razerCheckBox);
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");
        minPrice = findViewById(R.id.minPrice);
        maxPrice = findViewById(R.id.maxPrice);
        filterBtn = findViewById(R.id.filterButton);



        myAccessoriesList(); // Fetch the recyclerview
        filteredAccessories.addAll(accessoriesModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        accessoriesRecyclerView.setLayoutManager(linearLayoutManager);
        accessoriesAdapter = new AccessoriesAdapter(accessoriesModels, filteredAccessories, this);
        accessoriesRecyclerView.setAdapter(accessoriesAdapter);

        // Check if the checkboxes are checkable or not
        setCheckBoxesAndButtonAccessories();
    }

    private void myAccessoriesList() {
        accessoriesModels = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot items:snapshot.child("Categories").child("Accessories").getChildren()){
                    String namedb = items.child("Name").getValue(String.class);
                    String brnddb = items.child("Brand").getValue(String.class);
                    String pricedb = items.child("Price").getValue(String.class);
                    for (DataSnapshot snap:items.child("Picture").getChildren()){
//                    String imageDB=items.child("Picture").child("imageURL").getValue(String.class);
                        imageDB=snap.child("imageURL").getValue(String.class);
                    }
//                    String imageDB=items.child("Picture").child("imageURL").getValue(String.class);
                    String Title = brnddb+" "+namedb;
                    selectedItem.add(items.child("ID").getValue(String.class));
                    accessoriesModels.add(new AccessoriesModel(Title,imageDB,pricedb));

                }
                updateAccessoriesList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateAccessoriesList() {
        // Create a list to store the brands selected for filtering
        List<String> brandToFilter = new ArrayList<>();

        // Check which brand checkboxes are checked and add them to the brandToFilter list
        if (beatsCheckBox.isChecked()) brandToFilter.add("BEATS");
        if (jblCheckBox.isChecked()) brandToFilter.add("JBL");
        if (appleCheckBox.isChecked()) brandToFilter.add("APPLE");
        if (sonyCheckBox.isChecked()) brandToFilter.add("SONY");
        if (lenovoCheckBox.isChecked()) brandToFilter.add("LENOVO");
        if (googlesCheckBox.isChecked()) brandToFilter.add("GOOGLE");
        if (logitechCheckBox.isChecked()) brandToFilter.add("LOGITECH");
        if (razerCheckBox.isChecked()) brandToFilter.add("RAZER");

        // Filter the accessories based on the selected brands
        List<AccessoriesModel> filteredByBrand = new ArrayList<>();
        if (brandToFilter.isEmpty()) {
            filteredByBrand.addAll(accessoriesModels);
        } else {
            for (AccessoriesModel accessoriesModel : accessoriesModels) {
                for (String brand : brandToFilter) {
                    if (accessoriesModel.getAccessoriesTitle().toLowerCase().contains(brand.toLowerCase())) {
                        filteredByBrand.add(accessoriesModel);
                        break;
                    }
                }
            }
        }

        // Check if price filters are applied
        String minPriceText = minPrice.getText().toString();
        String maxPriceText = maxPrice.getText().toString();
        boolean isPriceFilterApplied = !TextUtils.isEmpty(minPriceText) || !TextUtils.isEmpty(maxPriceText);

        // If price filters are applied, filter the accessories by price range
        List<AccessoriesModel> finalFilteredAccessories = new ArrayList<>(filteredByBrand);
        if (isPriceFilterApplied) {
            try {
                int minPriceValue = TextUtils.isEmpty(minPriceText) ? Integer.MIN_VALUE : Integer.parseInt(minPriceText);
                int maxPriceValue = TextUtils.isEmpty(maxPriceText) ? Integer.MAX_VALUE : Integer.parseInt(maxPriceText);

                List<AccessoriesModel> filteredByPrice = new ArrayList<>();
                for (AccessoriesModel accessoriesModel : filteredByBrand) {
                    int accessoryPrice = Integer.parseInt(accessoriesModel.getAccessoriesPrice().replace("$", ""));
                    if (accessoryPrice >= minPriceValue && accessoryPrice <= maxPriceValue) {
                        filteredByPrice.add(accessoriesModel);
                    }
                }
                finalFilteredAccessories = filteredByPrice;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Update the filteredAccessories list with the final filtered accessories
        filteredAccessories.clear();
        filteredAccessories.addAll(finalFilteredAccessories);

        // Update the selectedItem list according to the filtered accessories
        List<String> updatedSelectedItem = new ArrayList<>();
        for (AccessoriesModel filteredAccessory : filteredAccessories) {
            for (AccessoriesModel originalModel : accessoriesModels) {
                if (filteredAccessory.getAccessoriesTitle().equals(originalModel.getAccessoriesTitle())) {
                    int index = accessoriesModels.indexOf(originalModel);
                    if (index < selectedItem.size()) {
                        updatedSelectedItem.add(selectedItem.get(index));
                    }
                    break;
                }
            }
        }
        selectedItem.clear();
        selectedItem.addAll(updatedSelectedItem);

        // Notify the adapter that the dataset has changed
        accessoriesAdapter.notifyDataSetChanged();
    }







    private void setCheckBoxesAndButtonAccessories() {
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAccessoriesList();
            }
        });

        beatsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateAccessoriesList();
            }
        });

        jblCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateAccessoriesList();
            }
        });
        appleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateAccessoriesList();
            }
        });

        sonyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateAccessoriesList();
            }
        });
        lenovoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateAccessoriesList();
            }
        });
        googlesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateAccessoriesList();

            }
        });

        logitechCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateAccessoriesList();
            }
        });

        razerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateAccessoriesList();
            }
        });
    }

    @Override
    public void onClick(int position) {
        // Handle item click if needed
        switch (position){
            default:
                Intent intent=new Intent(AccessoriesActivity.this, AccessoriesDetails.class);
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
