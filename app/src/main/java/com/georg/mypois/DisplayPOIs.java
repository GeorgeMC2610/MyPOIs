package com.georg.mypois;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DisplayPOIs extends AppCompatActivity implements SearchView.OnQueryTextListener
{
    TextView id, title, location, timeStamp, category, description, counter;
    FloatingActionButton buttonPrevious, buttonNext;
    ArrayList<POI> Pois;
    SearchView searchPoiByTitle;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pois);

        //TextViews
        id = findViewById(R.id.id);
        title = findViewById(R.id.Title);
        location = findViewById(R.id.Location);
        timeStamp = findViewById(R.id.Time);
        category = findViewById(R.id.Category);
        description = findViewById(R.id.Description);
        counter = findViewById(R.id.Counter);

        //Database
        Pois = MainActivity.poIsDatabaseManager.GetAllPois();

        //Buttons
        buttonPrevious = findViewById(R.id.buttonPrevious);
        buttonNext = findViewById(R.id.buttonNext);

        //SearchView
        searchPoiByTitle = findViewById(R.id.searchPoiByTitle);
        searchPoiByTitle.setOnQueryTextListener(this);

        //Disable the "previous" button (since the starting index is 0).
        buttonPrevious.setVisibility(FloatingActionButton.INVISIBLE);

        //Disable the "next" button if the are no POIs available.
        if (Pois.isEmpty())
            buttonNext.setVisibility(FloatingActionButton.INVISIBLE);

        UpdateLabels();
    }

    // this method updates all labels once a button is clicked.
    private void UpdateLabels()
    {
        // if there are no POIs available, it shows a message to the user.
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

        // otherwise it shows all information about a POI.
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

    // adjusting the index with the button. (Minus one)
    public void buttonPreviousClicked(View view)
    {
        index--;
        UpdateLabels();
    }

    // adjusting the index with the button. (Plus one)
    public void buttonNextClicked(View view)
    {
        index++;
        UpdateLabels();
    }

    // this is called when the delete button is pressed.
    public void buttonDeleteClicked(View view)
    {
        // build a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this P.O.I.?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                // if the user agrees to delete the POI, find the POI using its ID and delete it.
                MainActivity.poIsDatabaseManager.DeletePOI(Pois.get(index).getId());
                // then update all POIs.
                Pois = MainActivity.poIsDatabaseManager.GetAllPois();

                // adjust the index if it's out of bounds.
                if (Pois.isEmpty() || Pois.size() == 1)
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
                // DO NOTHING.
            }
        });
        builder.show();
    }

    public void buttonEditClicked(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Edit Fields.");
        builder.setMessage("Please enter the fields you want to edit.");

        final EditText editTitle = new EditText(this);
        builder.setView(editTitle);

        final Spinner editCategory = new Spinner(this);
        builder.setView(editCategory);

        final EditText description = new EditText(this);
        builder.setView(description);

        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                // EDIT FIELDS.
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                // DO NOTHING.
            }
        });
        builder.show();
    }

    @Override
    public boolean onQueryTextSubmit(String s)
    {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s)
    {
        index = 0;
        Pois = (s.isEmpty())? MainActivity.poIsDatabaseManager.GetAllPois() : MainActivity.poIsDatabaseManager.SearchPoiByTitle(s);
        UpdateLabels();

        return false;
    }
}