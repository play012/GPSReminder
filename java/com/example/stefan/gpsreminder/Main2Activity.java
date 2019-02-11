package com.example.stefan.gpsreminder;
// Codeautor Deniel Biletic
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.location.LocationManager.GPS_PROVIDER;

public class Main2Activity extends AppCompatActivity implements LocationListener {
    final int REQUEST_CODE = 43;
    double latitude = 0.00000;
    double longitude = 0.00000;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button neuePosition = findViewById(R.id.button3);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        Location location = null;
        onLocationChanged(location);

        if (location == null) {
            Toast.makeText(Main2Activity.this, "Position konnte nicht abgerufen werden.", Toast.LENGTH_SHORT).show();
        } else {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Toast.makeText(Main2Activity.this, "Breitengrad: " + latitude + "\nLängengrad: " + longitude, Toast.LENGTH_SHORT).show();
        }

        neuePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = getIntent();
                positionBekommen(result);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }

    public Intent positionBekommen(Intent res1){
        try{
            res1.putExtra("latitude", latitude);
            res1.putExtra("longitude", longitude);
            return res1;
        }catch(SecurityException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(GPS_PROVIDER, 0, 0, (android.location.LocationListener) this);
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