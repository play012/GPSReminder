package com.example.stefan.gpsreminder;
//Codeautor: Stefan Friesen
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CursorAdapter extends android.widget.CursorAdapter {

    public CursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor c) {
        TextView tV1 = view.findViewById(R.id.textView1);
        TextView tV2 = view.findViewById(R.id.textView2);
        TextView tV3 = view.findViewById(R.id.textView3);
        double latitude = c.getDouble(c.getColumnIndex("breitengrad"));
        double longitude = c.getDouble(c.getColumnIndex("laengengrad"));
        String date = c.getString(c.getColumnIndex("uhrzeit"));
        tV1.setText("Breitengrad: " + latitude);
        tV2.setText("Längengrad: " + longitude);
        tV3.setText("Uhrzeit: " + date);
    }
}
