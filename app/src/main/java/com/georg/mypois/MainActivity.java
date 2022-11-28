package com.georg.mypois;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
    }

    private void CreatePOIcards()
    {

    }

    // green button clicked
    public void buttonAddClicked(View view)
    {
        Intent intent = new Intent(this, ActivityAdd.class);
        startActivity(intent);
    }
}

