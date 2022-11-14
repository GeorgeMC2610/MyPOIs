package com.georg.mypois;

import android.location.Location;

import java.time.LocalDateTime;

public class POI
{
    private int id;
    private String name;
    private LocalDateTime timeStamp;
    private double latitude;
    private double longitude;
    private String category;
    private String description;

    public POI(String name, LocalDateTime timeStamp, double latitude, double longitude, String category, String description)
    {
        this.id = -1;
        this.name = name;
        this.timeStamp = timeStamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.description = description;
    }

    public POI(int id, String name, LocalDateTime timeStamp, double latitude, double longitude, String category, String description)
    {
        this.id = id;
        this.name = name;
        this.timeStamp = timeStamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.description = description;
    }

    //ID
    public int getId() {return id;}

    //NAME
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    //TIMESTAMP
    public LocalDateTime getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    //LATITUDE
    public double getLatitude()
    {
        return latitude;
    }

    public void setLocation(double latitude)
    {
        this.latitude = latitude;
    }

    //LONGITUDE
    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    //CATEGORY
    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    //DESCRIPTION
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
