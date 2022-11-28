package com.georg.mypois;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    public static POIsDatabaseManager poIsDatabaseManager;
    private SQLiteDatabase DB;
    ArrayList<POI> POIS;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DB = openOrCreateDatabase("myPOIS.db", MODE_PRIVATE, null);
        poIsDatabaseManager = new POIsDatabaseManager(DB);

        POIS = poIsDatabaseManager.GetAllPois();
        CreatePOIcards();
    }

    private void CreatePOIcards()
    {
        LinearLayout scrollViewLayout = findViewById(R.id.scrollViewLayout);
        LayoutInflater inflater = LayoutInflater.from(this);

        // create cards, each one containing a P.O.I.
        for (POI poi : POIS)
        {
            View view = inflater.inflate(R.layout.sample_poi_card, null);

            TextView title = view.findViewById(R.id.poi_title);
            TextView category = view.findViewById(R.id.poi_category);
            TextView datetime = view.findViewById(R.id.poi_date_time_captured);

            String date = poi.getTimeStamp().getDayOfMonth() + "/" + poi.getTimeStamp().getMonthValue() + "/" + poi.getTimeStamp().getYear() + " " + poi.getTimeStamp().getHour() + ":" + poi.getTimeStamp().getMinute();

            title.setText(poi.getName());
            category.setText(poi.getCategory());
            datetime.setText(date);

            scrollViewLayout.addView(view);
        }
    }

    // green button clicked
    public void buttonAddClicked(View view)
    {
        Intent intent = new Intent(this, ActivityAdd.class);
        startActivity(intent);
    }
}

