package com.q.caoimhin.satnap.services;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.q.caoimhin.satnap.activities.MapsActivity;

/**
 * Created by ecaoodo on 24/01/2018.
 */

public class LocationComparator {

    public boolean withinRange(Location currentLocation, Place destination, float range, Context context){

        Location destinationLocation = new Location("");
        destinationLocation.setLongitude(destination.getLatLng().longitude);
        destinationLocation.setLatitude(destination.getLatLng().latitude);

        if(currentLocation.distanceTo(destinationLocation)<= range){
            Toast.makeText(context, "we are within range", Toast.LENGTH_LONG)
                    .show();
            return true;
        }
        Toast.makeText(context, "we are not within range", Toast.LENGTH_LONG)
                .show();
        return false;
    }

}
