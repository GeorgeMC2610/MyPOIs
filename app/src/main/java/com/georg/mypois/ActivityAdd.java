package com.georg.mypois;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.time.LocalDateTime;

public class ActivityAdd extends AppCompatActivity implements LocationListener
{
    LocationManager locationManager;
    Location currentLocation;
    Spinner categoriesSpinner;
    EditText title;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // add back button and set custom title.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add A P.O.I.");

        // get views.
        title = findViewById(R.id.editTextTitle);
        categoriesSpinner = findViewById(R.id.spinnerCategory);
        description = findViewById(R.id.editTextTextMultiLineDescription);

        // get location permissions.
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        // all this just to add some items on a spinner?!
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, POI.Categories); // this is retrieved from a static field.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                back();
            default:
                return true;
        }
    }

    public void buttonInsertClicked(View view)
    {
        // check for empty text boxes.
        if (isAnyEditTextEmpty())
        {
            Toast.makeText(this, "In order to insert a P.O.I., you must first fill all fields.", Toast.LENGTH_LONG).show();
            return;
        }

        // check if permissions are not given.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "You must grant access to your location, in order to add a P.O.I.", Toast.LENGTH_LONG).show();
            return;
        }

        // Inform the user for duplicates.
        if (!MainActivity.poIsDatabaseManager.SearchPoiByTitle(title.getText().toString()).isEmpty())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Duplicate Found.");
            builder.setMessage("There is already a P.O.I. with the Title \"" + title.getText().toString() + "\". " +
                    "It is allowed to have multiple P.O.I.s with identical titles. Are you sure you want to proceed?");
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {

                }
            });
            builder.setPositiveButton("ADD ANYWAY", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    addPoiToDatabase();
                }
            });

            builder.show();
            return;
        }

        // sometimes the current location can be null.
        if (this.currentLocation == null)
        {
            Toast.makeText(this, "Move with your phone a little bit...", Toast.LENGTH_LONG).show();
            return;
        }

        // try to insert it in the database.
         if (addPoiToDatabase())
         {
             title.setText("");
             description.setText("");
         }
    }

    /**
     * Creates a {@link POI} object and adds it to the Database, using the {@link POIsDatabaseManager} class.
     * If any errors occur during this process, the program will display them in the description field.
     * @return False if something goes wrong or True if nothing fails.
     */
    private boolean addPoiToDatabase()
    {
        try
        {
            POI newPOI = new POI(title.getText().toString(), LocalDateTime.now(), currentLocation.getLatitude(), currentLocation.getLongitude(), categoriesSpinner.getSelectedItem().toString(), description.getText().toString());
            if (MainActivity.poIsDatabaseManager != null)
                MainActivity.poIsDatabaseManager.AddPOI(newPOI);

            Toast.makeText(this, "New Point Of Interest successfully inserted!", Toast.LENGTH_SHORT).show();
            return true;
        }
        catch (Exception e)
        {
            description.setText(e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * Internal function to check if there are any empty fields in the form.
     * This function is used commonly
     * @return
     */
    private boolean isAnyEditTextEmpty()
    {
        title.setText(title.getText().toString().trim());
        description.setText(description.getText().toString().trim());

        return (title.getText().toString().isEmpty() || description.getText().toString().isEmpty());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        this.currentLocation = location;
    }

    @Override
    public void onBackPressed()
    {
        back();
    }

    /**
     * Simple function that goes to the "previous" activity. Creates a new MainActivity object, so that it updates the P.O.I.s
     * if the user added new ones.
     */
    private void GoToPreviousActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Using an Alert Dialog, informs the user that they have filled fields and attempted to go back.
     * The user can choose between <b>staying in the "Add P.O.I." form</b> or <b>leaving the form.</b>
     * @see AlertDialog
     */
    private void back()
    {

        // if both edit texts are empty.
        if (title.getText().toString().isEmpty() && description.getText().toString().isEmpty())
        {
            // immediately go back.
            GoToPreviousActivity();
            return;
        }

        // otherwise, build an alert dialog and inform the user that there are filled fields.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Fields Filled");
        builder.setMessage("You have filled the fields with information. Are you sure you want to go back?");

        // if the user agrees to go back, take them back to the previous activity.
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                GoToPreviousActivity();
            }
        });

        // if the user doesn't agree, do nothing.
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                // DO NOTHING.
            }
        });

        builder.show();
    }
}