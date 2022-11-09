package com.georg.mypois;

import android.location.Location;

import java.time.LocalDateTime;

public class POI
{
    private String name;
    private LocalDateTime timeStamp;
    private Location location;
    private String category;
    private String description;

    public POI(String name, LocalDateTime timeStamp, Location location, String category, String description)
    {
        this.name = name;
        this.timeStamp = timeStamp;
        this.location = location;
        this.category = category;
        this.description = description;
    }

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

    //LOCATION
    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
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
