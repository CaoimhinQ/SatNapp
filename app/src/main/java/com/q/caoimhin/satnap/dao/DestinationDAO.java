package com.q.caoimhin.satnap.dao;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;

/**
 * Created by ecaoodo on 21/01/2018.
 */


public class DestinationDAO {

    private static DestinationDAO instance;
    private ArrayList<Place> places;

    private DestinationDAO(){places = new ArrayList<>();}

    public static synchronized DestinationDAO getInstance(){
        if(instance == null){
            instance = new DestinationDAO();
        }
        return instance;
    }

    public boolean addPlace(Place place) {
        if (places.contains(place)) {
            return false;
        }
        places.add(place);
        return true;
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }
}
