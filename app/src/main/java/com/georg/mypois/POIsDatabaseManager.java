package com.georg.mypois;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class POIsDatabaseManager
{
    private SQLiteDatabase DB;

    public POIsDatabaseManager(SQLiteDatabase DB)
    {
        this.DB = DB;
        DB.execSQL("CREATE TABLE IF NOT EXISTS POIS (" +
                "id INT PRIMARY KEY," +
                "name TEXT," +
                "time DATETIME," +
                "latitude REAL," +
                "longitude REAL," +
                "category TEXT," +
                "description TEXT)");
    }

    public int getCountOfPOIS()
    {
        String query = "SELECT COUNT(id) FROM POIS";
        Cursor cursor = DB.rawQuery(query, null);
        if (!cursor.moveToFirst())
            return 0;

        return cursor.getInt(0);
    }

    public void AddPOI(POI poi)
    {
        // initialize values
        int id;
        String name, category, description;
        LocalDateTime timeStamp;
        double latitude, longitude;

        // get values
        id = GenerateID();
        name = poi.getName();
        timeStamp = poi.getTimeStamp();
        category = poi.getCategory();
        description = poi.getDescription();
        latitude = poi.getLatitude();
        longitude = poi.getLongitude();

        //
        String query = "INSERT INTO POIS VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] bindArgs = new Object[]{GenerateID(), name, timeStamp, latitude, longitude, category, description};

        DB.execSQL(query, bindArgs);
    }

    public void DeletePOI(int id)
    {
        String query = "DELETE FROM POIS WHERE ID = ?";
        Object[] bindArgs = new Object[]{id};

        DB.execSQL(query, bindArgs);
    }

    public ArrayList<POI> GetAllPois()
    {
        ArrayList<POI> pois = new ArrayList<>();

        String query = "SELECT * FROM POIS";
        Cursor cursor = DB.rawQuery(query, null);

        while (cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String timeStamp = cursor.getString(2);
            double latitude = cursor.getDouble(3);
            double longitude = cursor.getDouble(4);
            String category = cursor.getString(5);
            String description = cursor.getString(6);

            POI poi = new POI(id, title, LocalDateTime.parse(timeStamp), latitude, longitude, category, description);
            pois.add(poi);
        }

        return pois;
    }

    private int GenerateID()
    {
        Random random = new Random();
        int potentialID = random.nextInt(999999);

        if (CheckIfIDExists(potentialID))
            return GenerateID();

        return potentialID;
    }

    private boolean CheckIfIDExists(int id)
    {
        Cursor cursor = DB.rawQuery("SELECT * FROM POIS WHERE ID = ?", new String[]{String.valueOf(id)});
        return (cursor.getCount() > 0);
    }
}
