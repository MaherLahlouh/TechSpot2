package com.example.techspot.PhonesActivity;

import android.app.ActivityOptions;
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
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techspot.Adapters.PhonesAdapter;
import com.example.techspot.BaseActivity.BaseActivity;
import com.example.techspot.Interfaces.MainRecyclerViewInterFace;
import com.example.techspot.Models.PhonesModel;
import com.example.techspot.PhoneAndLaptopDetails.DetailsActivity;
import com.example.techspot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PhonesActivity extends BaseActivity implements MainRecyclerViewInterFace {

    //Widget
    private RecyclerView phonesRecyclerView; // RecyclerView

    private PhonesAdapter phonesAdapter; // Object form the Phones Adapter

    private CheckBox iphone, samsung, xiaomi, huawei, realme, vivo, oppo, google; //checkBoxes
    private CheckBox honor, sony;//checkBoxes
    private String imageDB;

    private EditText minPrice, maxPrice; // Edit text to set minimum and maximum price
    private DatabaseReference reference;
    private Button filterBtn; // Filter Button


    //List of type <PhonesModel>
    private List<PhonesModel> phonesModels = new ArrayList<>();
    private List<PhonesModel> filteredPhones = new ArrayList<>();
    private List<String> selectedItem = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phones);
//**************************************************************************************
        //initialization
        phonesRecyclerView = findViewById(R.id.phonesRecyclerView);
        iphone = findViewById(R.id.iphoneCheckBox);
        samsung = findViewById(R.id.samsungCheckBox);
        xiaomi = findViewById(R.id.xiaomiCheckBox);
        huawei = findViewById(R.id.huaweiCheckBox);
        realme = findViewById(R.id.realmeCheckBox);
        vivo = findViewById(R.id.vivoCheckBox);
        oppo = findViewById(R.id.oppoCheckBox);
        google = findViewById(R.id.googleCheckBox);
        honor = findViewById(R.id.honorCheckBox);
        sony = findViewById(R.id.sonyCheckBox);
        minPrice = findViewById(R.id.minPrice);
        maxPrice = findViewById(R.id.maxPrice);
        filterBtn = findViewById(R.id.filterButton);
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");
        //-------------------------------------------------------------------------------

        myPhonesist(); //Function declare the fetch the recyclerview
        filteredPhones.addAll(phonesModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        phonesRecyclerView.setLayoutManager(linearLayoutManager);
        phonesAdapter = new PhonesAdapter(phonesModels, filteredPhones, this);
        phonesRecyclerView.setAdapter(phonesAdapter);
        phonesAdapter.setItemClickListener(this);
        //------------Check if the checkboxes is checkable or not ----------------------------
        setCheckBoxesPhones();
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePhonesList();
            }
        });
    }


    private void myPhonesist() {
        phonesModels = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot items : snapshot.child("Categories").child("Phones").getChildren()) {
                    String namedb = items.child("Name").getValue(String.class);
                    String brnddb = items.child("Brand").getValue(String.class);
                    String pricedb = items.child("Price").getValue(String.class);


                    for (DataSnapshot snap : items.child("Picture").getChildren()) {
//                    String imageDB=items.child("Picture").child("imageURL").getValue(String.class);
                        imageDB = snap.child("imageURL").getValue(String.class);
                    }
                    String Title = brnddb + " " + namedb;
                    selectedItem.add(items.child("ID").getValue(String.class));
                    phonesModels.add(new PhonesModel(Title, imageDB, pricedb));

                }
                updatePhonesList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void updatePhonesList() {
        // Create a list to store the brands selected for filtering
        List<String> brandToFilter = new ArrayList<>();

        // Check which brand checkboxes are checked and add them to the brandToFilter list
        if (iphone.isChecked()) brandToFilter.add("Iphone");
        if (samsung.isChecked()) brandToFilter.add("Samsung");
        if (sony.isChecked()) brandToFilter.add("Sony");
        if (xiaomi.isChecked()) brandToFilter.add("Xiaomi");
        if (honor.isChecked()) brandToFilter.add("Honor");
        if (oppo.isChecked()) brandToFilter.add("Oppo");
        if (vivo.isChecked()) brandToFilter.add("Vivo");
        if (google.isChecked()) brandToFilter.add("Google");
        if (realme.isChecked()) brandToFilter.add("Realme");
        if (huawei.isChecked()) brandToFilter.add("Huawei");

        // If no brand is selected, show all phones, else filter the phones by selected brands
        if (brandToFilter.isEmpty()) {
            filteredPhones.clear();
            filteredPhones.addAll(phonesModels);
        } else {
            List<PhonesModel> filteredByBrand = new ArrayList<>();

            // Iterate through phonesModels and add phones with selected brands to filteredByBrand list
            for (PhonesModel phoneModel : phonesModels) {
                boolean shouldInclude = false;
                for (String brand : brandToFilter) {
                    if (phoneModel.getPhoneTitle().toLowerCase().contains(brand.toLowerCase())) {
                        shouldInclude = true;
                        break;
                    }
                }
                if (shouldInclude) {
                    filteredByBrand.add(phoneModel);
                }
            }

            // Update filteredPhones list with phones filtered by brand
            filteredPhones.clear();
            filteredPhones.addAll(filteredByBrand);
        }

        // Update the selectedItem list according to the filtered phonesModels
        List<String> updatedSelectedItem = new ArrayList<>();
        for (PhonesModel phoneModel : filteredPhones) {
            for (PhonesModel originalModel : phonesModels) {
                if (phoneModel.getPhoneTitle().equals(originalModel.getPhoneTitle())) {
                    int index = phonesModels.indexOf(originalModel);
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

                List<PhonesModel> filteredByPrice = new ArrayList<>();
                for (PhonesModel phonemodel : filteredPhones) {
                    int phoneprice = Integer.parseInt(phonemodel.getPhonePrice().replace("$", ""));
                    if (phoneprice >= minPriceValue && phoneprice <= maxPriceValue) {
                        filteredByPrice.add(phonemodel);
                    }
                }

                // Update filteredPhones list with phones filtered by price
                filteredPhones.clear();
                filteredPhones.addAll(filteredByPrice);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Notify the adapter that the dataset has changed
        phonesAdapter.notifyDataSetChanged();
    }


    private void setCheckBoxesPhones() {
        iphone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePhonesList();
            }
        });
        samsung.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePhonesList();
            }
        });
        xiaomi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePhonesList();
            }
        });
        huawei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePhonesList();

            }
        });
        sony.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePhonesList();
            }
        });
        oppo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePhonesList();
            }
        });
        vivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePhonesList();
            }
        });
        realme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePhonesList();
            }
        });
        honor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePhonesList();
            }
        });
        google.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePhonesList();
            }
        });
    }


    @Override
    public void onClick(int position) {

        switch (position) {
            default:
                if (position >= 0 && position < filteredPhones.size()) {
                    Intent intent = new Intent(PhonesActivity.this, DetailsActivity.class);
                    String s = selectedItem.get(position);
                    String selectedItemImageUrl = filteredPhones.get(position).getPhoneImage(); // Assuming you have a method to get image URL in PhonesModel

                    intent.putExtra("ID", s);
                    intent.putExtra("CAT", "Phones");
                    intent.putExtra("IMAGE_URL", selectedItemImageUrl); // Pass the image URL to DetailsActivity
                    startActivity(intent);
                }
                break;

        }
    }


    public void searchFilter(String query) {
        filteredPhones.clear();
        if (TextUtils.isEmpty(query)) {
            filteredPhones.addAll(phonesModels);
        } else {
            String searchText = query.toLowerCase().trim();
            for (PhonesModel phone : phonesModels) {
                if (phone.getPhoneTitle().toLowerCase().contains(searchText)) {
                    filteredPhones.add(phone);
                }
            }
        }
        phonesAdapter.notifyDataSetChanged();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchViewBar);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFilter(newText);
                return true;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return true;
    }
}