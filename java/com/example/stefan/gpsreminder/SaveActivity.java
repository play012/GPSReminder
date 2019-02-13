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
import android.view.Gravity;
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

public class SaveActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {
    // Abfrage, Anzeige und Übergabe der aktuellen Position per GPS und Internet
    final int REQUEST_CODE = 43;
    double latitude;
    double longitude;
    Location location;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        Button neuePosition = findViewById(R.id.button3);
        // erneute GPS-Abfrage wegen des LocationManagers in dieser Klasse
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        //Abfrage der Position
        positionBekommen();
        // Anzeige des Breiten- und Längengrads als Toast
        Toast toast = Toast.makeText(SaveActivity.this, "Breitengrad: " + latitude + "\nLängengrad: " + longitude, Toast.LENGTH_SHORT);
        /*
         Toast soll über dem Button angezeigt werden (yOffset):
         https://developer.android.com/guide/topics/ui/notifiers/toasts
        */
        toast.setGravity(Gravity.BOTTOM, 0, 250);
        toast.show();

        /*
         SupportMapFragment für eine vereinfachte Map-Komponente in der Activity:
         https://developers.google.com/android/reference/com/google/android/gms/maps/SupportMapFragment
         */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        neuePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Längen- und Breitengrad über Intent zurückgeben
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
        // GoogleMap erzeugen
        GoogleMap gMap = googleMap;
        // Längen- und Breitengrad in das eigene Format LatLng legen
        LatLng pos1 = new LatLng(latitude, longitude);
        // Marker auf der GoogleMap setzen und "Aktuelle Position" benennen
        gMap.addMarker(new MarkerOptions().position(pos1).title("Aktuelle Position"));
        // Kamera der GoogleMap auf den Marker zentrieren
        gMap.moveCamera(CameraUpdateFactory.newLatLng(pos1));
        // GoogleMap zoomfähig einstellen
        UiSettings settings = gMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
    }

    public Location positionBekommen(){
        try{
            //LocationManager erzeugen
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            /*
             Location-Abfrage mit GPS_PROVIDER alleine war nicht ausreichend, der Code für beide
             Abfragen zusammen stammt von: https://stackoverflow.com/a/18532792

             Hierbei wurde die Methode public Location getLocation() teilweise übernommen und
             angepasst
             */

            // Überprüfen der Verfügbarkeit der Provider
            boolean isNetworkEnabled = lm.isProviderEnabled(lm.NETWORK_PROVIDER);
            boolean isGPSEnabled = lm.isProviderEnabled(lm.GPS_PROVIDER);

            // Abfrage der Location anhand des NETWORK_PROVIDERs
            if (isNetworkEnabled) {
                lm.requestLocationUpdates(lm.NETWORK_PROVIDER, 0, 0, this);
                if (lm != null) {
                    location = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
                    if (location != null) {
                        // Zuweisen des Längen- und Breitengrads
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }

            // Abfrage der Location anhand des GPS_PROVIDERs
            if (isGPSEnabled) {
                if (location == null) {
                    lm.requestLocationUpdates(lm.GPS_PROVIDER, 0, 0, this);
                    if (lm != null) {
                        location = lm.getLastKnownLocation(lm.GPS_PROVIDER);
                        if (location != null) {
                            // Zuweisen des Längen- und Breitengrads
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
        Toast.makeText(SaveActivity.this, "GPS ist aktiviert.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(SaveActivity.this, "Bitte GPS einschalten.", Toast.LENGTH_SHORT).show();
    }
}