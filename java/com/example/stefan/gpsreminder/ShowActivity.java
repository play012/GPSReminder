package com.example.stefan.gpsreminder;
// Codeautor: Deniel Biletic
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
    // Anzeige der ausgewählten Position auf einer Karte
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        // Intent der ListActivity abfangen (enthält Breiten- und Längengrad der ausgewählten Position)
        Intent int1 = getIntent();
        Bundle extras = int1.getExtras();

        latitude = extras.getDouble("latitude");
        longitude = extras.getDouble("longitude");

        // Anzeige von Längen- und Breitengrad als Toast
        Toast.makeText(ShowActivity.this, "Breitengrad: " + latitude + "\nLängengrad: " + longitude, Toast.LENGTH_SHORT).show();

        // Erneutes Erstellen eines SupportMapFragments (vereinfachte Mapkomponente, siehe SaveActivity)
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // GoogleMap erzeugen
        GoogleMap gMap = googleMap;
        // Längen- und Breitengrad in das eigene Format LatLng legen
        LatLng pos1 = new LatLng(latitude, longitude);
        // Marker auf der GoogleMap setzen und "Ausgewählte Position" benennen
        gMap.addMarker(new MarkerOptions().position(pos1).title("Ausgewählte Position"));
        // Kamera der GoogleMap auf den Marker zentrieren
        gMap.moveCamera(CameraUpdateFactory.newLatLng(pos1));
        // GoogleMap zoomfähig einstellen
        UiSettings settings = gMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
    }
}