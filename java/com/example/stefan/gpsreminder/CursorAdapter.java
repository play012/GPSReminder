package com.example.stefan.gpsreminder;
// Codeautor: Stefan Friesen
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CursorAdapter extends android.widget.CursorAdapter {
    // Definition des Cursoradapters für die Anzeige der Listenelemente
    public CursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Hier wird das Layout item.xml für Listenelemente festgelegt
        return LayoutInflater.from(context).inflate(R.layout.item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor c) {
        // Hier wird die Anzeige für jedes Listenelement definiert
        TextView tV1 = view.findViewById(R.id.textView1);
        TextView tV2 = view.findViewById(R.id.textView2);
        TextView tV3 = view.findViewById(R.id.textView3);
        double latitude = c.getDouble(c.getColumnIndex("breitengrad"));
        double longitude = c.getDouble(c.getColumnIndex("laengengrad"));
        String date = c.getString(c.getColumnIndex("uhrzeit"));
        tV1.setText("Breitengrad: " + latitude);
        tV2.setText("Längengrad: " + longitude);
        // Hier wird zusätzlich zur Position das Datum ausgegeben
        tV3.setText("Uhrzeit: " + date);
    }
}
