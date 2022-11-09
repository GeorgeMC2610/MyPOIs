package com.georg.mypois;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.time.LocalDateTime;

public class MainActivity extends AppCompatActivity
{
    POIsDatabaseManager poIsDatabaseManager;
    private SQLiteDatabase DB;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DB = openOrCreateDatabase("myPOIS.db", MODE_PRIVATE, null);

        poIsDatabaseManager = new POIsDatabaseManager(DB);
    }

    public void buttonAddNewClicked(View view)
    {
        Intent intent = new Intent(this, ActivityAdd.class);
        startActivity(intent);
    }

    public void buttonDeleteClicked(View view)
    {

    }
}