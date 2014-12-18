package com.example.chiang.busstop;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.chiang.busstop.Model.Bus;
import com.example.chiang.busstop.Model.TranslinkParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.List;


public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private List<Bus> myBusList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        new TranslinkClient().execute();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        // mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(49.17018, -123.13662)));
        if(myBusList != null) {
            includeAllMarkers(myBusList);
        }
        mMap.setBuildingsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    //include all bus markers in the given list
    private void includeAllMarkers(List<Bus> list) {
        if (list != null) {
            LatLngBounds.Builder bounds = new LatLngBounds.Builder();
            for (Bus bus : list) {
                bounds.include(new LatLng(bus.getLatitude(), bus.getLongitude()));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 10));
        } else{
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(49.263600, -123.139533))      // Sets the center of the map to Mountain View
                    .zoom(11)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private class TranslinkClient extends AsyncTask{
        TranslinkParser translink;
        @Override
        protected Object doInBackground(Object[] params) {
            Log.i("TranslinkClient", "start parser");
            translink = new TranslinkParser();
            translink.get();
            myBusList = translink.buslist;
            Log.i("TranslinkClient", "end parser");
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.i("TranslinkClient", "start drawing");
            for(Bus bus: myBusList){
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(bus.getLatitude(), bus.getLongitude()))
                        .title(bus.getDirection()));
                Log.i("Translink Info", bus.getDirection());
            }
            LatLngBounds.Builder bounds = new LatLngBounds.Builder();
            for (Bus bus : myBusList) {
                bounds.include(new LatLng(bus.getLatitude(), bus.getLongitude()));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50));
            Log.i("TranslinkClient", "end drawing");
        }
    }
}


