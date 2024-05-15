package com.example.techspot;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techspot.AccessoriesActivity.AccessoriesActivity;
import com.example.techspot.Adapters.MainAdapterView;
import com.example.techspot.BaseActivity.BaseActivity;
import com.example.techspot.CartActivity.CartActivity;
import com.example.techspot.ComputerActivity.ComputerActivity;
import com.example.techspot.Interfaces.MainRecyclerViewInterFace;
import com.example.techspot.LaptopActivity.LaptopActivity;
import com.example.techspot.MapActivity.MapActivity;
import com.example.techspot.PaymentActivity.PaymentActivity;
import com.example.techspot.PhonesActivity.PhonesActivity;
import com.example.techspot.SettingsActivity.SettingsActivity;
import com.example.techspot.SuggestionsActivity.SuggestionsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements MainRecyclerViewInterFace {

    RecyclerView recyclerView;
    List<String> myTitlesMain = new ArrayList<>();
    List<Integer> myImagesMain = new ArrayList<>();
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initializeViews();
//        setUpBottomNavigationView();
//        setUpRecyclerView();
//        startViewFlipper();
//        setUpMainRecyclerView();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        ViewFlipper viewFlipper = findViewById(R.id.viewFlipper);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.homeIcon);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.homeIcon) {
                    Toast.makeText(MainActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.settingsIcon) {
                    Toast.makeText(MainActivity.this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                    Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(settingsIntent);
                } else if (itemId == R.id.locationIcon) {
                    Toast.makeText(MainActivity.this, "Location Clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.shoppingCartIcon) {
                    Toast.makeText(MainActivity.this, "Shopping cart clicked", Toast.LENGTH_SHORT).show();
                    Intent shoppingIntent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(shoppingIntent);
                } else if (itemId == R.id.creditCardIcon) {
                Toast.makeText(MainActivity.this, "Credit Card", Toast.LENGTH_SHORT).show();
                Intent creditIntent = new Intent(MainActivity.this, PaymentActivity.class);
                startActivity(creditIntent);
                return true;
                } else {
                    return false;
                }
                return true;

            }
        });


        viewFlipper.startFlipping();

        //--------------------------------------------------RecyclerView Work Start Form here
        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        myTitlesMain.add(getString(R.string.phones));
        myTitlesMain.add(getString(R.string.laptops));
        myTitlesMain.add(getString(R.string.computers));
        myTitlesMain.add(getString(R.string.accessories));
        myTitlesMain.add(getString(R.string.suggestions));

        myImagesMain.add(R.drawable.smartphone);
        myImagesMain.add(R.drawable.laptop);
        myImagesMain.add(R.drawable.gaming);
        myImagesMain.add(R.drawable.phone_case);
        myImagesMain.add(R.drawable.suggestion);
        recyclerView.setAdapter(new MainAdapterView(myTitlesMain, myImagesMain, (MainRecyclerViewInterFace) this));
        recyclerView.setHasFixedSize(true);

    }

    @Override
    public void onClick(int position) {
        switch (position){
        case 0:
        //Move to Phone Activity.
        System.out.println("Position 0");
        Intent intentPhones = new Intent(MainActivity.this, PhonesActivity.class);
        startActivity(intentPhones);
        break;

        case 1:
        //Move to Laptop Activity.
        System.out.println("Position 1");
        Intent laptopIntent=new Intent(MainActivity.this, LaptopActivity.class);
        startActivity(laptopIntent);
        break;

        case 2:
        //Move to Computer Activity.
        System.out.println("Position 2");
        Intent computerIntent=new Intent(MainActivity.this, ComputerActivity.class);
        startActivity(computerIntent);
        break;

        case 3:
        //Move to Accessories Activity.
        System.out.println("Position 3");
        Intent accessoriesIntent=new Intent(MainActivity.this, AccessoriesActivity.class);
        startActivity(accessoriesIntent);
        break;

        case 4:
        //Move to Suggestions Activity.
        Intent suggestionIntent = new Intent(MainActivity.this, SuggestionsActivity.class);
        startActivity(suggestionIntent);
        break;

        default:
        System.out.println("Else Statement");

    }

    }
}