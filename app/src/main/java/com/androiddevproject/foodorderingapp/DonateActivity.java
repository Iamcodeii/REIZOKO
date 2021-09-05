package com.androiddevproject.foodorderingapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DonateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button addProduct_btn;
    EditText name_box, brand_box;
    Spinner spinner;
    DatabaseReference databaseReference;

    FrameLayout chooseImage_btn;
    public static ImageView imageView;
    private static final int GET_IMAGE_REQUEST_CODE = 100;
    Uri imageFilePath;
    Bitmap imageToStore;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;
    private StorageReference reference = FirebaseStorage.getInstance().getReference();


    private ProgressBar progressBar;


    FrameLayout locationPicker_btn;
    TextView location_tv;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    FusedLocationProviderClient fusedLocationProviderClient;
    Double lat;
    Double longt;
    String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        addProduct_btn = findViewById(R.id.addProduct_btn);
        name_box = findViewById(R.id.name_box);
        brand_box = findViewById(R.id.brand_box);
        spinner = findViewById(R.id.spinner_addCategs);
        chooseImage_btn = findViewById(R.id.addImage_layout);
        progressBar = findViewById(R.id.progressbar);
        locationPicker_btn = findViewById(R.id.location_btn);
        location_tv = findViewById(R.id.location_tv);
        imageView = findViewById(R.id.crtImage_iv);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        chooseImage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GET_IMAGE_REQUEST_CODE);
            }
        });

        SharedPreferences preferences = getSharedPreferences("USER",MODE_PRIVATE);
        if (preferences.contains("phone")){
            phone = preferences.getString("phone","");
        }else {
            Toast.makeText(this, "PLease login first", Toast.LENGTH_LONG).show();
            finish();
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationPicker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (getApplicationContext().checkSelfPermission(false)){
//
//                    }
                    if (ActivityCompat.checkSelfPermission(DonateActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DonateActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(DonateActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);

                        return;
                    }
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                lat = location.getLatitude();
                                longt = location.getLongitude();
                                Log.e("TAG", lat + " , " + longt);
                                location_tv.setText("Location Identification Success");
                                location_tv.setTextColor(getResources().getColor(R.color.green_color));

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "LOCATION GET FAILED");
                        }
                    });
                }


            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String categs = parent.getItemAtPosition(position).toString();

        addProduct_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categs.equals("Select Category")) {
                    Toast.makeText(DonateActivity.this, "Select Category", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imageView.getDrawable() == null) {
                    Toast.makeText(DonateActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = name_box.getText().toString();
                String qtyStr = brand_box.getText().toString();

                if (name.equals("")) {
                    Toast.makeText(DonateActivity.this, "Please Enter Name of the product", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (qtyStr.equals("")) {
                    Toast.makeText(DonateActivity.this, "Please Enter Quanitity of the product", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (location_tv.getText().toString().equals("Please give your Location")){
                    Toast.makeText(DonateActivity.this, "PLease provide Location", Toast.LENGTH_SHORT).show();
                    return;
                }
                int qty = Integer.parseInt(qtyStr);
                if (qty == 0){
                    Toast.makeText(DonateActivity.this, "Quantity should be greater than 1", Toast.LENGTH_SHORT).show();
                    return;
                }


                final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(imageFilePath));
                fileRef.putFile(imageFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                long random = System.currentTimeMillis();
                                String id = "donationfood" + random;
                                DonationProduct products =new DonationProduct(name,lat+"",longt+"",uri.toString(),id,categs,phone,qty);
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("DonationProducts");
                                databaseReference.child(id).setValue(products).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(DonateActivity.this, "Donation Product Added", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        addProduct_btn.setVisibility(View.VISIBLE);
                                        imageView.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
                                        finish();
                            }
                        });

                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        addProduct_btn.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        addProduct_btn.setVisibility(View.VISIBLE);
                        Toast.makeText(DonateActivity.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageFilePath = data.getData();
            try {
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                imageView.setImageBitmap(imageToStore);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private String getFileExtension(Uri mUri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    lat = location.getLatitude();
                                    longt = location.getLongitude();
                                    Log.e("TAG", lat + " , " + longt);
                                    location_tv.setText("Location Identification Success");
                                    location_tv.setTextColor(getResources().getColor(R.color.green_color));

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("TAG", "LOCATION GET FAILED");
                            }
                        });
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}