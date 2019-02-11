package com.example.stefan.gpsreminder;
// Codeautor Stefan Friesen
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final int REQUEST_CODE = 42;
    private GPSDBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbOpenHelper = GPSDBOpenHelper.getInstance(this);
        Button b1 = findViewById(R.id.button);
        Button b2 = findViewById(R.id.button2);

        b1.setOnClickListener(new View.OnClickListener() { // Neue Position abspeichern
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(MainActivity.this, Main2Activity.class);
                startActivityForResult(int1, REQUEST_CODE);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() { // Alle Positionen abrufen
            @Override
            public void onClick(View v) {
                double latitude = 0.00000;
                double longitude = 0.00000;
                Intent int2 = new Intent(MainActivity.this, Main3Activity.class);
                double[] columns = new double[]{latitude, longitude};
                Cursor c = dbOpenHelper.query("gps", columns, null, null, null, null, null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    do {
                        double lat1 = c.getDouble(c.getColumnIndex("latitude"));
                        double long1 = c.getDouble(c.getColumnIndex("longitude"));
                        double[] position = new double[]{lat1, long1};
                        int2.putExtra("position" + c.getCount(), position);
                    } while(c.moveToNext());
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
                double latitude = data.getDoubleExtra("latitude", 0.00000);
                double longitude = data.getDoubleExtra("longitude", 0.00000);
                dbOpenHelper.insertDataset(latitude, longitude);
            }
        }
    }
}