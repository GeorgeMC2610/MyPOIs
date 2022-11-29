package com.georg.mypois;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

public class DisplayPOIs extends AppCompatActivity
{
    TextView id, title, location, timeStamp, category, description;
    int poi_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pois);

        // add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TextViews
        id = findViewById(R.id.id);
        title = findViewById(R.id.Title);
        location = findViewById(R.id.Location);
        timeStamp = findViewById(R.id.Time);
        category = findViewById(R.id.Category);
        description = findViewById(R.id.Description);

        GetSharedPreferences();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            default:
                return true;
        }
    }

    private void GetSharedPreferences()
    {
        SharedPreferences sharedPref = getSharedPreferences("Main_Activity", Context.MODE_PRIVATE);
        poi_id = sharedPref.getInt("POI_ID", 0);

        POI poi = MainActivity.poIsDatabaseManager.GetPoiByID(poi_id);

        id.setText(String.valueOf(poi.getId()));
        title.setText(poi.getName());
        location.setText(poi.getLatitude() + ", " + poi.getLongitude());
        timeStamp.setText(poi.getTimeStamp().toString());
        category.setText(poi.getCategory());
        description.setText(poi.getDescription());
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
                MainActivity.poIsDatabaseManager.DeletePOI(poi_id);

                Intent intent = new Intent(DisplayPOIs.this, MainActivity.class);
                startActivity(intent);
                finish();
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

        for (int i = 0; i < 9; i++)
            spinner_dictionary.put(POI.Categories[i], i);

        categories.setSelection(spinner_dictionary.get(category.getText().toString()));

        builder.setView(v);

        // this is for toast.
        Context context = this;

        // in case the user hits confirm
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

                GetSharedPreferences();
            }
        });

        // in case the user hits "cancel"
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
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}