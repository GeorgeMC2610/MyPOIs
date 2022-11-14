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

public class MainActivity extends AppCompatActivity
{
    public static POIsDatabaseManager poIsDatabaseManager;
    private SQLiteDatabase DB;
    TextView numberOfPOIS;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DB = openOrCreateDatabase("myPOIS.db", MODE_PRIVATE, null);
        poIsDatabaseManager = new POIsDatabaseManager(DB);

        numberOfPOIS = findViewById(R.id.textViewCountPOIS);


        String POIS;
        int numberOfPois = poIsDatabaseManager.getCountOfPOIS();
        switch(numberOfPois)
        {
            case 0:
                POIS = "No P.O.I.s yet! Add yours now, by clicking the button below.";
                break;
            case 1:
                POIS = "One P.O.I. available.";
                break;
            default:
                POIS = numberOfPois + " P.O.I.s available.";
                break;
        }

        numberOfPOIS.setText(POIS);
    }

    // green button clicked
    public void buttonAddNewClicked(View view)
    {
        try
        {
            Intent intent = new Intent(this, ActivityAdd.class);
            startActivity(intent);
        }
        catch (Exception e)
        {
            TextView textView = findViewById(R.id.textViewWelcome);
            textView.setText(e.getMessage());
        }
    }

    // blue button clicked
    public void buttonDisplayAllPOIsClicked(View view)
    {
        try
        {
            Intent intent = new Intent(this, DisplayPOIs.class);
            startActivity(intent);
        }
        catch (Exception e)
        {
            TextView textView = findViewById(R.id.textViewWelcome);
            textView.setText(e.getMessage());
        }
    }
}