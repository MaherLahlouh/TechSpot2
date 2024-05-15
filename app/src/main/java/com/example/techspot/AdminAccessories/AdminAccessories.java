package com.example.techspot.AdminAccessories;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techspot.Adapters.ImageAdapter;
import com.example.techspot.Models.adminModel;
import com.example.techspot.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class AdminAccessories extends AppCompatActivity {

    private EditText adminAccessoriesBrand, adminAccessoriesItem, accessoriesCompatibleDevice, accessoriesMaterial
            , accessoriesFactor, accessoriesScreenSize, accessoriesSpecialFeature, accessoriesCharger,
            accessoriesCables, accessoriesBatteryCapacity, accessoriesItemPrice, accessoriesQuantity;
    private Button addToDB, clearData;
    private ImageView accessoriesPic;
    private DatabaseReference reference;
    private StorageReference storage;
    private ArrayList<Uri> ImageUri = new ArrayList<>();

    private RecyclerView rvImages;
    private ImageAdapter adapter;
    private ArrayList<Uri> imagesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_accessories);

        initializeViews();
        clickListeners();

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
            adapter = new ImageAdapter(imagesArrayList);
            rvImages.setAdapter(adapter);
        }
    }

    private void initializeViews() {
        adminAccessoriesBrand = findViewById(R.id.adminAccessoriesBrand);
        adminAccessoriesItem = findViewById(R.id.adminAccessoriesItem);
        accessoriesCompatibleDevice = findViewById(R.id.accessoriesCompatibleDevice);
        accessoriesMaterial = findViewById(R.id.accessoriesMaterial);
        accessoriesFactor = findViewById(R.id.accessoriesFactor);
        accessoriesScreenSize = findViewById(R.id.accessoriesScreenSize);
        accessoriesSpecialFeature = findViewById(R.id.accessoriesSpecialFeature);
        accessoriesCharger = findViewById(R.id.accessoriesCharger);
        accessoriesCables = findViewById(R.id.accessoriesCables);
        accessoriesBatteryCapacity = findViewById(R.id.accessoriesBatteryCapacity);
        accessoriesItemPrice = findViewById(R.id.accessoriesItemPrice);
        addToDB = findViewById(R.id.addToDB);
        accessoriesPic = findViewById(R.id.accessoriesPic);
        accessoriesQuantity = findViewById(R.id.accessoriesQuantity);
        clearData = findViewById(R.id.clear_button);
        rvImages = findViewById(R.id.imagesRV);
        storage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://techspot-fc2c4.appspot.com");
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");
    }

    private void clickListeners() {
        accessoriesPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), 2);
            }
        });

        addToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItem();
            }
        });

        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearDataEntity();
            }
        });
    }

    private void clearDataEntity() {
        adminAccessoriesBrand.setText("");
        adminAccessoriesItem.setText("");
        accessoriesCompatibleDevice.setText("");
        accessoriesMaterial.setText("");
        accessoriesFactor.setText("");
        accessoriesScreenSize.setText("");
        accessoriesSpecialFeature.setText("");
        accessoriesCharger.setText("");
        accessoriesCables.setText("");
        accessoriesBatteryCapacity.setText("");
        accessoriesItemPrice.setText("");
        accessoriesQuantity.setText("");
        accessoriesPic.setImageResource(R.drawable.baseline_add_photo_alternate_24);
        imagesArrayList.clear();
        adapter.notifyDataSetChanged(); // Notify adapter about the change
    }

    private void AddItem() {
        String accessoriesBrandName = adminAccessoriesBrand.getText().toString().toUpperCase(Locale.ROOT);
        String accessoriesName = adminAccessoriesItem.getText().toString().toUpperCase(Locale.ROOT);
        String accessoriesCompatibleDev = accessoriesCompatibleDevice.getText().toString();
        String accessoriesMater = accessoriesMaterial.getText().toString();
        String accessoriesFact = accessoriesFactor.getText().toString();
        String accessoriesScrSize = accessoriesScreenSize.getText().toString();
        String accessoriesSpecialFeat = accessoriesSpecialFeature.getText().toString();
        String accessoriesCharge = accessoriesCharger.getText().toString();
        String accessoriesCable = accessoriesCables.getText().toString();
        String accessoriesBattCapacity = accessoriesBatteryCapacity.getText().toString();
        String accessoriesPrice = accessoriesItemPrice.getText().toString();
        int accessoriesQuantity1 = 1;
        try {
            String quantityString = accessoriesQuantity.getText().toString();
            accessoriesQuantity1 = Integer.parseInt(quantityString);
        } catch (NumberFormatException e) {
            Toast.makeText(AdminAccessories.this, "Please enter a valid quantity (number)", Toast.LENGTH_SHORT).show();
            return;
        }
        String category = "Accessories";
        int finalAccessoriesQuantity = accessoriesQuantity1;

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String finalid = setId(category);
                boolean x = true;
                for (DataSnapshot items : snapshot.child("Categories").child(category).getChildren()) {
                    String dbbrand = items.child("Brand").getValue(String.class);
                    String dbname = items.child("Name").getValue(String.class);
                    if (Objects.equals(dbbrand, accessoriesBrandName) && Objects.equals(dbname, accessoriesName)) {
                        x = false;
                        break;
                    }
                }
                if (x) {
                    reference.child("Categories").child(category).child(finalid).child("Brand").setValue(accessoriesBrandName);
                    reference.child("Categories").child(category).child(finalid).child("Name").setValue(accessoriesName);
                    reference.child("Categories").child(category).child(finalid).child("Price").setValue(accessoriesPrice);
                    reference.child("Categories").child(category).child(finalid).child("Compatible Devices").setValue(accessoriesCompatibleDev);
                    reference.child("Categories").child(category).child(finalid).child("Material").setValue(accessoriesMater);
                    reference.child("Categories").child(category).child(finalid).child("Factor").setValue(accessoriesFact);
                    reference.child("Categories").child(category).child(finalid).child("Screen Size").setValue(accessoriesScrSize);
                    reference.child("Categories").child(category).child(finalid).child("Special Features").setValue(accessoriesSpecialFeat);
                    reference.child("Categories").child(category).child(finalid).child("Charger").setValue(accessoriesCharge);
                    reference.child("Categories").child(category).child(finalid).child("Cables").setValue(accessoriesCable);
                    reference.child("Categories").child(category).child(finalid).child("Battery Capacity").setValue(accessoriesBattCapacity);
                    reference.child("Categories").child(category).child(finalid).child("Quantity").setValue(finalAccessoriesQuantity); //added
                    reference.child("Categories").child(category).child(finalid).child("ID").setValue(finalid);

                    Image_database_action(imagesArrayList, finalid);


                } else {
                    Toast.makeText(AdminAccessories.this, "This item is already in the Database!!!", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminAccessories.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String setId(String cat) {
        String subcat = cat.substring(0, 3);
        String id = subcat + System.currentTimeMillis();
        return id;
    }

    private void Image_database_action(ArrayList<Uri> imageUris, String finalid) {
        String cat = "Accessories";

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
                            Toast.makeText(AdminAccessories.this, "Image not uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminAccessories.this, "Image Upload failed, Please try again Later", Toast.LENGTH_SHORT).show();
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
}