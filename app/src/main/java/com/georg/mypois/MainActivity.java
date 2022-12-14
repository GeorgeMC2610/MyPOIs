package com.georg.mypois;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener
{
    public static POIsDatabaseManager poIsDatabaseManager;
    private SQLiteDatabase DB;
    SearchView searchViewPois;
    ArrayList<POI> POIS;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("My P.O.I.s");

        searchViewPois = findViewById(R.id.SearchViewPOIs);
        searchViewPois.setOnQueryTextListener(this);

        DB = openOrCreateDatabase("myPOIS.db", MODE_PRIVATE, null);
        poIsDatabaseManager = new POIsDatabaseManager(DB);

        POIS = poIsDatabaseManager.GetAllPois();
        CreatePoiCards();
    }

    /**
     * Creates all the cards with {@link POI} objects.
     * The cards can be pressed and show more details about a {@link POI} if so.
     * Every card has two Buttons, button Delete and button Edit.
     */
    private void CreatePoiCards()
    {
        // this is to inflate all cards
        LinearLayout scrollViewLayout = findViewById(R.id.scrollViewLayout);
        LayoutInflater inflater = LayoutInflater.from(this);

        // delete all cards before placing new ones
        scrollViewLayout.removeAllViews();

        ScrollView scrollView = findViewById(R.id.scrollViewPois);
        TextView textViewEmpty = findViewById(R.id.textViewEmpty);
        TextView textViewHint = findViewById(R.id.textViewHint1);

        // set the button or the scroll view to be invisible, according to the number of POIs available.
        scrollView.setVisibility(POIS.isEmpty()? View.GONE : View.VISIBLE);
        textViewEmpty.setVisibility(POIS.isEmpty()? View.VISIBLE : View.GONE);
        textViewHint.setVisibility(POIS.isEmpty()? View.GONE : View.VISIBLE);

        // create cards, each one containing a P.O.I.
        for (POI poi : POIS)
        {
            // inflate each card the layout
            View view = inflater.inflate(R.layout.sample_poi_card, null);

            CardView cardView = view.findViewById(R.id.cardViewPOI);
            cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    SharedPreferences sharedPreferences = getSharedPreferences("Main_Activity", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("POI_ID", poi.getId());
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, DisplayPOIs.class);
                    startActivity(intent);
                    finish();
                }
            });

            // deconstruct the views
            TextView title = view.findViewById(R.id.poi_title);
            TextView category = view.findViewById(R.id.poi_category);
            TextView datetime = view.findViewById(R.id.poi_date_time_captured);

            // deconstruct the buttons.
            FloatingActionButton buttonDelete = view.findViewById(R.id.buttonDelete);
            FloatingActionButton buttonEdit = view.findViewById(R.id.buttonEdit);

            // Add Listener for the Delete Button
            buttonDelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    DeletePOI(poi);
                }
            });

            // Add Listener for the Edit Button
            buttonEdit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    EditPOI(poi);
                }
            });

            // build a reasonable date.
            String date = poi.getTimeStamp().getDayOfMonth() + "/" + poi.getTimeStamp().getMonthValue() + "/" + poi.getTimeStamp().getYear() + " " + poi.getTimeStamp().getHour() + ":" + (poi.getTimeStamp().getMinute() < 10? "0" + poi.getTimeStamp().getMinute() : poi.getTimeStamp().getMinute());

            // set the values of the edit texts.
            title.setText(poi.getName());
            category.setText(poi.getCategory());
            datetime.setText(date);

            // add the view.
            scrollViewLayout.addView(view);
        }
    }

    // green button clicked
    public void buttonAddClicked(View view)
    {
        Intent intent = new Intent(this, ActivityAdd.class);
        startActivity(intent);
        finish();
    }

    private void DeletePOI(POI poi)
    {
        // build a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this P.O.I.?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                // if the user agrees to delete the POI, find the POI using its ID and delete it.
                MainActivity.poIsDatabaseManager.DeletePOI(poi.getId());

                // then update all POIs.
                POIS = MainActivity.poIsDatabaseManager.GetAllPois();
                CreatePoiCards();
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

    private void EditPOI(POI poi)
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
        Title.setText(poi.getName());
        Description.setText(poi.getDescription());

        // create the spinner so it has all the different categories
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, POI.Categories); // this is retrieved from a static field.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner categories = v.findViewById(R.id.spinner2);
        categories.setAdapter(adapter);

        // also se the spinner to be selected with the editing item
        Hashtable<String, Integer> spinner_dictionary = new Hashtable<String, Integer>();

        for (int i = 0; i < 9; i++)
            spinner_dictionary.put(POI.Categories[i], i);

        categories.setSelection(spinner_dictionary.get(poi.getCategory()));

        builder.setView(v);

        // this is for toast.
        Context context = this;

        // in case the user hits confirm
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
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
                MainActivity.poIsDatabaseManager.EditPOI(poi.getId(), newTitle, newCategory, newDescription);
                POIS = poIsDatabaseManager.GetAllPois();
                CreatePoiCards();
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
    public boolean onQueryTextSubmit(String s)
    {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s)
    {
        if (s.isEmpty())
        {
            POIS = poIsDatabaseManager.GetAllPois();
            CreatePoiCards();
            return false;
        }

        POIS = poIsDatabaseManager.SearchPoiByTitle(s);
        CreatePoiCards();
        return false;
    }
}


