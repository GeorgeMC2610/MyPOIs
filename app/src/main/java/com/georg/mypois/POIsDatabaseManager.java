package com.georg.mypois;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.time.LocalDateTime;
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
        latitude = poi.getLocation().getLatitude();
        longitude = poi.getLocation().getLongitude();

        //
        String query = "INSERT INTO POIS VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] bindArgs = new Object[]{GenerateID(), name, timeStamp, latitude, longitude, category, description};

        DB.execSQL(query, bindArgs);
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
