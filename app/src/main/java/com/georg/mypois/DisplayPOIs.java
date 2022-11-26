package com.georg.mypois;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

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

        Pois = MainActivity.poIsDatabaseManager.GetAllPois();

        //TextViews
        id = findViewById(R.id.id);
        title = findViewById(R.id.Title);
        location = findViewById(R.id.Location);
        timeStamp = findViewById(R.id.Time);
        category = findViewById(R.id.Category);
        description = findViewById(R.id.Description);
        counter = findViewById(R.id.Counter);

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

        UpdateLabels("");
    }

    // this method updates all labels once a button is clicked.
    private void UpdateLabels(String query)
    {
        Pois = (query.isEmpty()? MainActivity.poIsDatabaseManager.GetAllPois() : MainActivity.poIsDatabaseManager.SearchPoiByTitle(query));

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
        UpdateLabels("");
    }

    // adjusting the index with the button. (Plus one)
    public void buttonNextClicked(View view)
    {
        index++;
        UpdateLabels("");
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
                else if (index >= Pois.size())
                    index = Pois.size() - 1;

                UpdateLabels("");
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
        // create an alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Edit Fields.");
        builder.setMessage("Please enter the fields you want to edit.");

        // inflate the views and put them in the dialog
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.edit_properties, null);

        // get the views
        EditText Title = v.findViewById(R.id.newTitle);
        EditText Description = v.findViewById(R.id.newDescription);

        // set the view's values to be pre-set
        Title.setText(title.getText().toString());
        Description.setText(description.getText().toString());

        // create the spinner so it has all the different categories
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, POI.Categories); // this is retrieved from a static field.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner categories = v.findViewById(R.id.spinner2);
        categories.setAdapter(adapter);

        // also se the spinner to be selected with the editing item
        Hashtable<String, Integer> spinner_dictionary = new Hashtable<String, Integer>();
        spinner_dictionary.put(POI.Categories[0], 0);
        spinner_dictionary.put(POI.Categories[1], 1);
        spinner_dictionary.put(POI.Categories[2], 2);
        spinner_dictionary.put(POI.Categories[3], 3);
        spinner_dictionary.put(POI.Categories[4], 4);
        spinner_dictionary.put(POI.Categories[5], 5);
        spinner_dictionary.put(POI.Categories[6], 6);
        spinner_dictionary.put(POI.Categories[7], 7);
        spinner_dictionary.put(POI.Categories[8], 8);

        categories.setSelection(spinner_dictionary.get(category.getText().toString()));

        builder.setView(v);

        // this is for toast.
        Context context = this;

        // in case the
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                int edit_id = Integer.parseInt(id.getText().toString());
                String newTitle = Title.getText().toString();
                String newCategory = categories.getSelectedItem().toString();
                String newDescription = Description.getText().toString();

                // and if any of them are empty, abort editing.
                if (newTitle.isEmpty() || newDescription.isEmpty())
                {
                    Toast.makeText(context, "Aborting edit, fields are empty.", Toast.LENGTH_LONG).show();
                    return;
                }

                // if not, update the labels.
                MainActivity.poIsDatabaseManager.EditPOI(edit_id, newTitle, newCategory, newDescription);
                UpdateLabels("");
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
        UpdateLabels(s);

        return false;
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}