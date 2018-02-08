package com.q.caoimhin.satnap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.gohool.goomaps.locationandmap.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.q.caoimhin.satnap.dao.DestinationDAO;

public class DestinationSelection extends AppCompatActivity {

    DestinationDAO destinationDAO;

    int PLACE_PICKER_REQUEST = 1;
    public static final String EXTRA_MESSAGE = "com.q.caoimhin.satnap.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        destinationDAO = DestinationDAO.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    userSelectsStop();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//

            }
        });

    }

    private void userSelectsStop() throws GooglePlayServicesNotAvailableException {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            Intent myPlacePickerIntent = builder.build(this);
            startActivityForResult(myPlacePickerIntent, PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                LatLng destination = place.getLatLng();
                destinationDAO.addPlace(place);

                // move to map activity
                Intent intent = new Intent(this, MapsActivity.class);
                String message = "we have moved from destination selection activity";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);

                // set other parameters like distance or time or alarm type
            }
        }
    }

}
