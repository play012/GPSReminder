package com.example.stefan.gpsreminder;
//Codeautor: Deniel Biletic
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowActivity extends AppCompatActivity implements OnMapReadyCallback {
    int cursorPosition;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent int1 = getIntent();
        Bundle extras = int1.getExtras();

        cursorPosition = extras.getInt("cursorPosition");
        latitude = extras.getDouble("latitude");
        longitude = extras.getDouble("longitude");

        Toast.makeText(ShowActivity.this, "Breitengrad: " + latitude + "\nLängengrad: " + longitude, Toast.LENGTH_SHORT).show();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap gMap = googleMap;
        LatLng pos1 = new LatLng(latitude, longitude);
        gMap.addMarker(new MarkerOptions().position(pos1).title("Ausgewählte Position"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(pos1));
        UiSettings settings = gMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
    }
}