package com.example.techspot.AdminActivity;

import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techspot.Adapters.ImageAdapter;
import com.example.techspot.AdminAccessories.AdminAccessories;
import com.example.techspot.AdminComputers.AdminComputers;
import com.example.techspot.DiscountsAdmin.DiscountsAdmin;
import com.example.techspot.LoginActivity.LoginActivity;
import com.example.techspot.Models.adminModel;
import com.example.techspot.R;
import com.example.techspot.UserCommentActivity.UserCommentActivity;
import com.example.techspot.itemQuantityAdmin.itemQuantityAdmin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class adminActivity extends AppCompatActivity {
    EditText  Brand , Name , CPU,GPU,FrontCamera,BackCamera,Battery,Display,OS,RAM,Memory,Price,phonesLaptopsQuantity;
    ImageView ItemPic;
    Spinner category;
    Button Add;
    FirebaseAuth auth;
    DatabaseReference reference;
    StorageReference storage;
    Uri ImageUri;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    private RecyclerView rvImages;//*CHANGED***************
    private ImageAdapter adapter;//*CHANGED***************
    private ArrayList<Uri> imagesArrayList=new ArrayList<>();//*CHANGED***************


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        category = findViewById(R.id.category_spinner);
        Brand = findViewById(R.id.adminBrand);
        Name = findViewById(R.id.adminItem);
        CPU = findViewById(R.id.aCPU);
        GPU = findViewById(R.id.aGPU);
        Battery = findViewById(R.id.aBattery);
        FrontCamera = findViewById(R.id.aFrontCamera);
        BackCamera = findViewById(R.id.aBackCamera);
        Display = findViewById(R.id.aDisplay);
        OS = findViewById(R.id.aOperatingSystem);
        RAM = findViewById(R.id.aRAM);
        Memory = findViewById(R.id.aMemory);
        Price = findViewById(R.id.ItemPrice);
        ItemPic = findViewById(R.id.imageView_admin);
        Add = findViewById(R.id.button2);
        rvImages=findViewById(R.id.imagesRV);
        phonesLaptopsQuantity=findViewById(R.id.phonesLaptopsQuantity);
        auth = FirebaseAuth.getInstance();
        //--------- seeUsersComments ------------- Click Listener
        drawerLayout=findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.itemsCouponsAndDiscounts) {
                    Intent intent2 = new Intent(adminActivity.this, DiscountsAdmin.class);
                    startActivity(intent2);
                } else if (itemId == R.id.changeItemsQuantity) {
                    Intent intent1 = new Intent(adminActivity.this, itemQuantityAdmin.class);
                    startActivity(intent1);
                } else if (itemId == R.id.showUserSuggestions) {
                    Intent intent = new Intent(adminActivity.this, UserCommentActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.adminLogout) {
                    adminLogout();
                } else if (itemId == R.id.addAccessories) {
                    Intent intentAccessories = new Intent(adminActivity.this, AdminAccessories.class);
                    startActivity(intentAccessories);
                } else if (itemId == R.id.addComputers) {
                    Intent intentComputers = new Intent(adminActivity.this, AdminComputers.class);
                    startActivity(intentComputers);
                } else {
                    System.out.println("Hello");
                }
                return false;

            }
        });


        //---------------------------------------------------------

        //-------------------------------------------------------------------------------------------------
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(adminActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.catname));
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        category.setAdapter(adapter);
        storage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://techspot-fc2c4.appspot.com");
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");
        ItemPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"Select Picture"), 2);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Additem();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imagesArrayList.add(imageUri);
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                imagesArrayList.add(imageUri);
            }
            rvImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            adapter=new ImageAdapter(imagesArrayList);
            rvImages.setAdapter(adapter);
        }
    }


    private void Additem() {
        String brnd = Brand.getText().toString().toUpperCase(Locale.ROOT);
        String nm = Name.getText().toString().toUpperCase(Locale.ROOT);
        String cpu = CPU.getText().toString();
        String gpu = GPU.getText().toString();
        String batt = Battery.getText().toString();
        String frontc = FrontCamera.getText().toString();
        String backc = BackCamera.getText().toString();
        String display = Display.getText().toString();
        String os = OS.getText().toString();
        String ram = RAM.getText().toString();
        String memory = Memory.getText().toString();
        String cat = category.getSelectedItem().toString();
        String prce = Price.getText().toString();

        int quantity1 = 1;
        try {
            String quantityString = phonesLaptopsQuantity.getText().toString();
            quantity1 = Integer.parseInt(quantityString);
        } catch (NumberFormatException e) {
            Toast.makeText(adminActivity.this, "Please enter a valid quantity (number)", Toast.LENGTH_SHORT).show();
            return;
        }

        int finalQuantity = quantity1;

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String finalid = setid(cat);
                boolean x = true;
                for(DataSnapshot items:snapshot.child("Categories").child(cat).getChildren()){
                    String dbbrand = items.child("Brand").getValue(String.class);
                    String dbname = items.child("Name").getValue(String.class);
                    if(Objects.equals(dbbrand,brnd)&&Objects.equals(dbname,nm)){
                        x =false;
                        break;
                    }
                }



                if(x){
                    reference.child("Categories").child(cat).child(finalid).child("Brand").setValue(brnd);
                    reference.child("Categories").child(cat).child(finalid).child("Name").setValue(nm);
                    reference.child("Categories").child(cat).child(finalid).child("Price").setValue(prce);
                    reference.child("Categories").child(cat).child(finalid).child("CPU").setValue(cpu);
                    reference.child("Categories").child(cat).child(finalid).child("GPU").setValue(gpu);
                    reference.child("Categories").child(cat).child(finalid).child("Battery").setValue(batt);
                    reference.child("Categories").child(cat).child(finalid).child("Front Camera").setValue(frontc);
                    reference.child("Categories").child(cat).child(finalid).child("Back Camera").setValue(backc);
                    reference.child("Categories").child(cat).child(finalid).child("Display").setValue(display);
                    reference.child("Categories").child(cat).child(finalid).child("OS").setValue(os);
                    reference.child("Categories").child(cat).child(finalid).child("RAM").setValue(ram);
                    reference.child("Categories").child(cat).child(finalid).child("Memory").setValue(memory);
                    reference.child("Categories").child(cat).child(finalid).child("Quantity").setValue(finalQuantity); //added
                    reference.child("Categories").child(cat).child(finalid).child("ID").setValue(finalid);

                    Image_database_action(imagesArrayList, finalid);

                }else{
                    Toast.makeText(adminActivity.this,"This item is already in the Database!!!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(adminActivity.this,"Something went wrong , Please try again later",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private String setid(String cat) {
        String subcat = cat.substring(0,3);
        String id = subcat+System.currentTimeMillis();
        return id;
    }



    private void Image_database_action(ArrayList<Uri> imageUris, String finalid) {
        String cat = category.getSelectedItem().toString();

        for (Uri imageUri : imageUris) {
            StorageReference file = storage.child(System.currentTimeMillis() + "." + getFile(imageUri));
            file.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            adminModel imagemodel = new adminModel(uri.toString());
                            reference.child("Categories").child(cat).child(finalid).child("Picture").push().setValue(imagemodel);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(adminActivity.this, "Image not uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(adminActivity.this, "Image Upload failed, Please try again Later", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String getFile(Uri imageUri) {
        // Get the file extension from the image URI
        ContentResolver cr = getContentResolver();
        MimeTypeMap m = MimeTypeMap.getSingleton();
        return m.getExtensionFromMimeType(cr.getType(imageUri));
    }

    private void showerror(EditText txt,String err) {
        txt.setError(err);
        txt.requestFocus();
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    private void adminLogout() {
        new AlertDialog.Builder(adminActivity.this)
                .setMessage(getString(R.string.logout_message))
                .setCancelable(false)
                .setPositiveButton(R.string.yes_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(adminActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Close current activity after logout
                    }
                })
                .setNegativeButton(R.string.no_btn, null)
                .show();
    }
}


