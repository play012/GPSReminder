package com.example.stefan.gpsreminder;
// Codeautor: Stefan Friesen
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class ListActivity extends AppCompatActivity {
    // Auflisten aller in der Datenbank gespeicherten Positionen
    private GPSDBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        dbOpenHelper = GPSDBOpenHelper.getInstance(this);
        // Erzeugen eines ListViews für alle in der Datenbank gespeicherten Positionen
        ListView lV1 = findViewById(R.id.listView1);

        // Abfrage aller Elemente in der Datenbank
        String[] columns1 = new String[]{"laengengrad", "breitengrad", "uhrzeit"};
        Cursor c1 = dbOpenHelper.query("gps", columns1, null, null, null, null, null);

        /*
         Um den ListView mit Daten aus der SQLite-Datenbank zu befüllen, wird der Cursor mit einem
         CursorAdapter verbunden, der dann den ListView mit den Daten vom Cursor befüllt.

         Datenquelle (Cursor) -> Adapter -> Anzeige (ListView)

         Hierbei wird eine neue Klasse CursorAdapter erzeugt, um das Layout und den Inhalt von jedem
         Element im ListView festzulegen.
         */
        final CursorAdapter cAdapter = new CursorAdapter(this, c1);
        lV1.setAdapter(cAdapter);

        // Beim Klicken auf ein Listenelement soll sich eine Karte mit der ausgewählten Position öffnen
        lV1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Cursor bekommt die Position des ListView-Elements
                Cursor c2 = (Cursor) parent.getItemAtPosition(position);
                Intent intPos = new Intent(ListActivity.this, ShowActivity.class);
                // Übergabe des Breiten- und Längengrads aus der Datenbank
                double latitude = c2.getDouble(c2.getColumnIndex("breitengrad"));
                double longitude = c2.getDouble(c2.getColumnIndex("laengengrad"));
                intPos.putExtra("latitude", latitude);
                intPos.putExtra("longitude", longitude);
                // Starten der Activity zur Kartenanzeige
                startActivity(intPos);
            }
        });
    }
}