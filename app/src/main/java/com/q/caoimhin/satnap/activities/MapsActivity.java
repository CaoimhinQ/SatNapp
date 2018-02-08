package com.q.caoimhin.satnap.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.gohool.goomaps.locationandmap.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.q.caoimhin.satnap.dao.DestinationDAO;
import com.q.caoimhin.satnap.services.LocationComparator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private DestinationDAO destinationDAO;
    int PLACE_PICKER_REQUEST = 1;
    private LocationComparator locationComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationComparator = new LocationComparator();
        }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        destinationDAO = DestinationDAO.getInstance();
//        try {
//            userSelectsStop();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("ecaoodo: ", location.toString());
                mMap.clear(); // clears our map

                // Add a marker for current location and move the camera
                LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
                //mMap.addMarker(new MarkerOptions().position(newLocation).title("New Location"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 10));

                Place selectedDestination = destinationDAO.getPlaces().get(0);


                if(locationComparator.withinRange(location, selectedDestination, 500, MapsActivity.this)) {
                    Log.d("Caoimhin: ", "caoimhin! time to wake up!");

                        Toast.makeText(MapsActivity.this, "caoimhin! time to wake up!", Toast.LENGTH_LONG)
                                .show();
                } else{
                    Toast.makeText(MapsActivity.this, "caoimhin! enjoy your rest, we're not within range yet", Toast.LENGTH_LONG)
                            .show();
                }


                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);

                    String fullAddress = "";

                    if (addressList != null && addressList.size() > 0) {

                        Log.d("Address: ", addressList.get(0).toString());

//                        Toast.makeText(MapsActivity.this, addressList.get(0).getAddressLine(0), Toast.LENGTH_LONG)
//                                .show();

                        if (addressList.get(0).getAddressLine(0) != null) {
                            fullAddress += addressList.get(0).getAddressLine(0) + " ";

                        }
                        if (addressList.get(0).getSubAdminArea() != null) {
                            fullAddress += addressList.get(0).getSubAdminArea() + " ";
                        }


                        Toast.makeText(MapsActivity.this, "Address+" + fullAddress, Toast.LENGTH_LONG)
                                .show();

                    } else {
                        Log.d("Address:", "Couldn't find Address");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.d("ecaoodo: ", "we have checked the permission");
                return;
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }else {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Ask for permission
                Log.d("ecaoodo: ", "requesting permission");
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }else {
                // we have permission!
                Log.d("ecaoodo: ", "we have permission");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
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
                mMap.addMarker(new MarkerOptions().position(destination).title("New Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 10));
            }
        }
    }

    private void userSelectsStop() throws GooglePlayServicesNotAvailableException{
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            Intent myPlacePickerIntent = builder.build(this);
            startActivityForResult(myPlacePickerIntent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,  locationListener);

        }

    }

}
