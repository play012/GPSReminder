package com.example.stefan.gpsreminder;
// Codeautor Deniel Biletic
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Main2Activity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {
    final int REQUEST_CODE = 43;
    double latitude;
    double longitude;
    Location location;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button neuePosition = findViewById(R.id.button3);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        positionBekommen();
        Toast.makeText(Main2Activity.this, "Breitengrad: " + latitude + "\nLängengrad: " + longitude, Toast.LENGTH_SHORT).show();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        neuePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = getIntent();
                result.putExtra("latitude", latitude);
                result.putExtra("longitude", longitude);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap gMap = googleMap;
        LatLng pos1 = new LatLng(latitude, longitude);
        gMap.addMarker(new MarkerOptions().position(pos1).title("Aktuelle Position"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(pos1));
        UiSettings settings = gMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
    }

    public Location positionBekommen(){
        try{
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean isNetworkEnabled = lm.isProviderEnabled(lm.NETWORK_PROVIDER);
            boolean isGPSEnabled = lm.isProviderEnabled(lm.GPS_PROVIDER);

            if (isNetworkEnabled) {
                lm.requestLocationUpdates(lm.NETWORK_PROVIDER, 0, 0, this);
                if (lm != null) {
                    location = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }

            if (isGPSEnabled) {
                if (location == null) {
                    lm.requestLocationUpdates(lm.GPS_PROVIDER, 0, 0, this);
                    if (lm != null) {
                        location = lm.getLastKnownLocation(lm.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }
        }catch(SecurityException e){
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location){
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){
    }

    @Override
    public void onProviderEnabled(String provider){
        Toast.makeText(Main2Activity.this, "GPS ist aktiviert.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(Main2Activity.this, "Bitte GPS einschalten.", Toast.LENGTH_SHORT).show();
    }
}