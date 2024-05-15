package com.example.techspot.AdminComputers;

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

public class AdminComputers extends AppCompatActivity {

    private EditText computerBrandName, computerName,computerPrice , computerQuantity,computerDescription;
    private Button addToDB,clearBtn;
    private ImageView computerPic;
    private DatabaseReference reference;
    private StorageReference storage;
    private RecyclerView rvImages;
    private ImageAdapter adapter;
    private ArrayList<Uri> imagesArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_computers);

        initializeViews();
        clickListeners();
    }

    private void initializeViews() {
        computerBrandName = findViewById(R.id.computerBrandName);
        computerName = findViewById(R.id.computerItemName);
        computerPrice = findViewById(R.id.computersPrice);
        addToDB = findViewById(R.id.addToDB);
        computerPic = findViewById(R.id.computerPic);
        computerDescription=findViewById(R.id.computerDescription);
        computerQuantity=findViewById(R.id.computerQuantity);
        clearBtn=findViewById(R.id.clear_button);
        rvImages=findViewById(R.id.imagesRV);
        storage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://techspot-fc2c4.appspot.com");
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");
    }
    private void clickListeners() {
        computerPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"Select Picture"), 2);
            }
        });

        addToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItem();
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
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

            // Initialize the adapter if it is not already initialized
            if (adapter == null) {
                rvImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                adapter = new ImageAdapter(imagesArrayList);
                rvImages.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }


    private void clearData(){
        // clear the brand edit text.
        computerBrandName.setText("");
        // clear the name edit text.
        computerName.setText("");

        computerDescription.setText("");

        computerPrice.setText("");
        // clear the gpu edit text.
        // clear the quantity edit text.
        computerQuantity.setText("");
//        computerPic.setImageResource(R.drawable.baseline_add_photo_alternate_24);
        // Clearing images from the RecyclerView
        imagesArrayList.clear();
        // Notify adapter about the change
        adapter.notifyDataSetChanged();

    }
    private void AddItem() {
        String computerBrand_Name = computerBrandName.getText().toString().toUpperCase(Locale.ROOT);
        String computer_Name = computerName.getText().toString().toUpperCase(Locale.ROOT);
        String computerPrices = computerPrice.getText().toString();
        String description=computerDescription.getText().toString();
        int computerQuantity1 = 1;
        try {
            String quantityString = computerQuantity.getText().toString();
            computerQuantity1 = Integer.parseInt(quantityString);
        } catch (NumberFormatException e) {
            Toast.makeText(AdminComputers.this, "Please enter a valid quantity (number)", Toast.LENGTH_SHORT).show();
            return;
        }
        String category = "Computers";

        int finalComputerQuantity = computerQuantity1;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String finalid = setId(category);
                boolean x = true;
                for (DataSnapshot items : snapshot.child("Categories").child(category).getChildren()) {
                    String dbbrand = items.child("Brand").getValue(String.class);
                    String dbname = items.child("Name").getValue(String.class);
                    String dbDescription = items.child("Description").getValue(String.class);
                    if (Objects.equals(dbbrand, computerBrand_Name) && Objects.equals(dbname, computer_Name)) {
                        x = false;
                        break;
                    }
                }
                if (x) {
                    reference.child("Categories").child(category).child(finalid).child("Brand").setValue(computerBrand_Name);
                    reference.child("Categories").child(category).child(finalid).child("Name").setValue(computer_Name);
                    reference.child("Categories").child(category).child(finalid).child("Description").setValue(description);
                    reference.child("Categories").child(category).child(finalid).child("Price").setValue(computerPrices);
                    reference.child("Categories").child(category).child(finalid).child("Quantity").setValue(finalComputerQuantity); //added
                    reference.child("Categories").child(category).child(finalid).child("ID").setValue(finalid);


                    // Upload multiple images
                    Image_database_action(imagesArrayList, finalid);
                } else {
                    Toast.makeText(AdminComputers.this, "This item is already in the Database!!!", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminComputers.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String setId(String cat) {
        String subcat = cat.substring(0, 3);
        String id = subcat + System.currentTimeMillis();
        return id;
    }
    private void Image_database_action(ArrayList<Uri> imageUris, String finalid) {
        String cat = "Computers";

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
                            Toast.makeText(AdminComputers.this, "Image not uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminComputers.this, "Image Upload failed, Please try again Later", Toast.LENGTH_SHORT).show();
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