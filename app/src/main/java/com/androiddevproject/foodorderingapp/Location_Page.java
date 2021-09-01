package com.androiddevproject.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class Location_Page extends AppCompatActivity  implements OnMapReadyCallback {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    FusedLocationProviderClient fusedLocationProviderClient;
    String latStr;
    String longtStr;

    Button next_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location__page);
        next_btn = findViewById(R.id.next_btn);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (getApplicationContext().checkSelfPermission(false)){
//
//                    }
            if (ActivityCompat.checkSelfPermission(Location_Page.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Location_Page.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(Location_Page.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                return;
            }
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        latStr = location.getLatitude()+"";
                        longtStr =location.getLongitude()+"";
                        Log.e("TAG", latStr +" , "+ longtStr);
                        SupportMapFragment supportMapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.location_map);
                        supportMapFragment.getMapAsync(Location_Page.this);
//                        location_tv.setText("Location Identification Success");
//                        location_tv.setTextColor(getResources().getColor(R.color.green_color));

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG","LOCATION GET FAILED");
                }
            });
        }

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!latStr.equals("") && !longtStr.equals("")){
                    Intent intent = new Intent(Location_Page.this, CheckOut_Page.class);
                    intent.putExtra("lat",latStr);
                    intent.putExtra("longt",longtStr);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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
                        if (location != null){
                            latStr = location.getLatitude()+"";
                            longtStr =location.getLongitude()+"";
                            Log.e("TAG", latStr +" , "+ longtStr);
                            SupportMapFragment supportMapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.location_map);
                            supportMapFragment.getMapAsync(Location_Page.this);
//                            location_tv.setText("Location Identification Success");
//                            location_tv.setTextColor(getResources().getColor(R.color.green_color));

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG","LOCATION GET FAILED");
                    }
                });
            }

        } else {
            Toast.makeText(this, "You denied for the location access", Toast.LENGTH_SHORT).show();
            // permission denied, boo! Disable the
            // functionality that depends on this permission.

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Double lata = Double.parseDouble(latStr);
        Double longta = Double.parseDouble(longtStr);

        LatLng latLng =new LatLng(lata, longta);
//        LatLng latLng =new LatLng(30.005630,73.257950);
        MarkerOptions markerOptions=new MarkerOptions().position(latLng)
                .title("You are Here");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        googleMap.addMarker(markerOptions);
    }
}