package com.example.stefan.gpsreminder;
// Codeautor Stefan Friesen
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    final int REQUEST_CODE = 42;
    private GPSDBOpenHelper dbOpenHelper;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        dbOpenHelper = GPSDBOpenHelper.getInstance(this);
        Button b1 = findViewById(R.id.button);
        Button b2 = findViewById(R.id.button2);

        b1.setOnClickListener(new View.OnClickListener() { // Neue Position abspeichern
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(MenuActivity.this, SaveActivity.class);
                startActivityForResult(int1, REQUEST_CODE);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() { // Alle Positionen abrufen
            @Override
            public void onClick(View v) {
                Intent int2 = new Intent(MenuActivity.this, ListActivity.class);
                String[] columns = new String[]{"laengengrad", "breitengrad", "uhrzeit"};
                Cursor c = dbOpenHelper.query("gps", columns, null, null, null, null, null);
                if (c.getCount() > 0) {
                    startActivity(int2);
                } else {
                    Toast.makeText(getApplicationContext(), "Datenbank ist leer.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                double latitude = data.getDoubleExtra("latitude", 0.0);
                double longitude = data.getDoubleExtra("longitude", 0.0);
                dbOpenHelper.insertDataset(latitude, longitude);
            }
        }
    }
}