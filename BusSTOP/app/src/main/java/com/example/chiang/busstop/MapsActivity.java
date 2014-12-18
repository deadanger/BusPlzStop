package com.example.chiang.busstop;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.chiang.busstop.Model.Bus;
import com.example.chiang.busstop.Model.TranslinkParser;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.List;


public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

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



    }


    private class TranslinkClient extends AsyncTask{
        TranslinkParser translink;
        @Override
        protected Object doInBackground(Object[] params) {
            Log.i("TranslinkClient", "start parser");
            translink = new TranslinkParser();
            translink.get();
            Log.i("TranslinkClient", "end parser");
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.i("TranslinkClient", "start drawing");
            for(Bus bus: translink.buslist){
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(bus.getLatitude(), bus.getLongitude()))
                        .title(bus.getDirection()));
                Log.i("Translink Info", bus.getDirection());
            }
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(0, 0))
                    .title("default"));
            Log.i("TranslinkClient", "end drawing");
        }
    }

}


