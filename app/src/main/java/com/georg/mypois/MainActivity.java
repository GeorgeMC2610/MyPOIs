package com.georg.mypois;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    ScrollView pois;
    CardView sampleCardView;
    TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pois = findViewById(R.id.poi_scrollview);
        sampleCardView = findViewById(R.id.SampleCardView);
        text1 = findViewById(R.id.text1);
    }

    private View cardViewCreator()
    {
        CardView cardView = new CardView(getApplicationContext());
        cardView = sampleCardView;
        return cardView;
    }

    public void AddPOI(View view)
    {
        CardView cardView = new CardView(this);
    }
}