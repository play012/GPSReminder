package com.example.stefan.gpsreminder;
// Codeautor Stefan Friesen
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GPSDBOpenHelper extends SQLiteOpenHelper {
    // SQLite-Datenbank aller gespeicherter Positionen
    private static GPSDBOpenHelper instance = null;
    private final static String DB_NAME = "GPSDatenbank";
    private final static int DB_VERSION = 1;

    private final String TABLE_NAME_GPS = "positionen";
    private final String COL_NAME_KEY = "_id";
    private final String COL_NAME_LATITUDE = "breitengrad";
    private final String COL_NAME_LONGITUDE = "laengengrad";
    private final String COL_NAME_DATETIME = "uhrzeit";

    //privater Konstruktor
    private GPSDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Erzeugung des Singleton-Musters
     * @param context Kontext der Instanz
     * @return Instanz
     */
    public static GPSDBOpenHelper getInstance (Context context){
        if (instance == null) {
            instance = new GPSDBOpenHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Tabelle in der Datenbank erzeugen
        String command = "CREATE TABLE " + TABLE_NAME_GPS + " ( " +
                COL_NAME_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_NAME_LATITUDE + " REAL NOT NULL ," +
                COL_NAME_LONGITUDE + " REAL NOT NULL," +
                COL_NAME_DATETIME + " TEXT )";

        try {
            sqLiteDatabase.execSQL(command);
        } catch (SQLException ex){
            Log.d(DB_NAME, "Fehler bei execSQL: Command : " + command);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GPS);
            this.onCreate(sqLiteDatabase);
        }
    }

    /**
     * Einen Datensatz einfügen
     * @param latitude Breitengrad der Position
     * @param longitude Längengrad der Position
     */

    public void insertDataset(double latitude, double longitude) {
        //Instanz der Datenbank holen
        SQLiteDatabase sqLiteDB = getWritableDatabase();
        ContentValues values = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /*
         SQLite arbeitet ausschließlich mit dem ISO-8601 Datumsformat:
         https://sqlite.org/lang_datefunc.html
         */
        Date date =  new Date();

        values.put(COL_NAME_LATITUDE, latitude);
        values.put(COL_NAME_LONGITUDE, longitude);
        values.put(COL_NAME_DATETIME, dateFormat.format(date));

        try {
            sqLiteDB.insertOrThrow(TABLE_NAME_GPS, null, values);
        } catch (SQLException ex) {
            Log.d(DB_NAME, "Fehler bei insert: Values : " + values.toString());
        }
    }

    /**
     * Datenbankabfrage aller Datensätze
     * @return Cursor mit der Ausgabe
     * @param gps
     * @param columns
     * @param o
     * @param o1
     * @param o2
     * @param o3
     * @param o4
     */
    public Cursor query(String gps, String[] columns, Object o, Object o1, Object o2, Object o3, Object o4) {
        SQLiteDatabase sqLiteDB = getReadableDatabase();
        // Der Query wird immer nach neuestem Datum sortiert
        Cursor resultSetCursor = sqLiteDB.query(TABLE_NAME_GPS, null, null,
                null, null, null, COL_NAME_DATETIME + " DESC");
        return resultSetCursor;
    }
}
