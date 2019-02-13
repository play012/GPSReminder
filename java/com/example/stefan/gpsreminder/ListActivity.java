package com.example.stefan.gpsreminder;
//Codeautor: Stefan Friesen
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class ListActivity extends AppCompatActivity {
    private GPSDBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        dbOpenHelper = GPSDBOpenHelper.getInstance(this);
        ListView lV1 = findViewById(R.id.listView1);

        String[] columns1 = new String[]{"laengengrad", "breitengrad", "uhrzeit"};
        Cursor c1 = dbOpenHelper.query("gps", columns1, null, null, null, null, null);
        final CursorAdapter cAdapter = new CursorAdapter(this, c1);
        lV1.setAdapter(cAdapter);

        lV1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c2 = (Cursor) parent.getItemAtPosition(position);
                Intent intPos = new Intent(ListActivity.this, ShowActivity.class);
                double latitude = c2.getDouble(c2.getColumnIndex("breitengrad"));
                double longitude = c2.getDouble(c2.getColumnIndex("laengengrad"));
                intPos.putExtra("latitude", latitude);
                intPos.putExtra("longitude", longitude);
                intPos.putExtra("cursorPosition", position);
                startActivity(intPos);
            }
        });
    }
}