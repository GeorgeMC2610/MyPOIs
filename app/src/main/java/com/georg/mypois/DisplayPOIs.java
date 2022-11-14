package com.georg.mypois;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DisplayPOIs extends AppCompatActivity
{
    TextView id, title, location, timeStamp, category, description, counter;
    FloatingActionButton buttonPrevious, buttonNext;
    ArrayList<POI> Pois;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pois);

        id = findViewById(R.id.id);
        title = findViewById(R.id.Title);
        location = findViewById(R.id.Location);
        timeStamp = findViewById(R.id.Time);
        category = findViewById(R.id.Category);
        description = findViewById(R.id.Description);
        counter = findViewById(R.id.Counter);

        Pois = MainActivity.poIsDatabaseManager.GetAllPois();

        buttonPrevious = findViewById(R.id.buttonPrevious);
        buttonNext = findViewById(R.id.buttonNext);

        buttonPrevious.setVisibility(FloatingActionButton.INVISIBLE);

        if (Pois.isEmpty())
            buttonNext.setVisibility(FloatingActionButton.INVISIBLE);

        UpdateLabels();
    }

    private void UpdateLabels()
    {
        if (index == 0 && Pois.isEmpty())
        {
            id.setText("---");
            title.setText("No P.O.I.s found yet!");
            location.setText("Go to the previous screen");
            timeStamp.setText("in order to add a new P.O.I.");
            category.setText("When you add a P.O.I.");
            description.setText("It will show up here.");
            counter.setText("No P.O.I.s.");
            return;
        }

        id.setText(String.valueOf(Pois.get(index).getId()));
        title.setText(Pois.get(index).getName());
        location.setText("At: " + String.valueOf(Pois.get(index).getLatitude()) + ", " + String.valueOf(Pois.get(index).getLatitude()));
        timeStamp.setText("Captured: " + Pois.get(index).getTimeStamp().toString());
        category.setText(Pois.get(index).getCategory());
        description.setText(Pois.get(index).getDescription());

        counter.setText(String.valueOf(index + 1) + " out of " + String.valueOf(Pois.size()));

        buttonPrevious.setVisibility((index == 0)? FloatingActionButton.INVISIBLE : FloatingActionButton.VISIBLE);
        buttonNext.setVisibility((index == Pois.size() - 1)? FloatingActionButton.INVISIBLE : FloatingActionButton.VISIBLE);

    }

    public void buttonPreviousClicked(View view)
    {
        index--;
        UpdateLabels();
    }

    public void buttonNextClicked(View view)
    {
        index++;
        UpdateLabels();
    }

    public void buttonDeleteClicked(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this P.O.I.?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                MainActivity.poIsDatabaseManager.DeletePOI(Pois.get(index).getId());
                Pois = MainActivity.poIsDatabaseManager.GetAllPois();

                if (Pois.isEmpty())
                    index = 0;
                else if (index > Pois.size())
                    index = Pois.size() - 1;

                UpdateLabels();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //DO NOTHING.
            }
        });
        builder.show();
    }
}