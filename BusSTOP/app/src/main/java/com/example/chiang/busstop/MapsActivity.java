package com.example.chiang.busstop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chiang.busstop.Model.Bus;
import com.example.chiang.busstop.Model.TranslinkParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private List<Bus> myBusList;
    private Map<Marker,Bus> displayList;
    private TranslinkParser translink;
    private Button routeChoice;
    private Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getRouteNo();
        routeChoice = (Button) findViewById(R.id.routeChoice);
        routeChoice.bringToFront();
        routeChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRouteNo();
                Toast.makeText(getApplicationContext(), "new route", Toast.LENGTH_SHORT).show();
            }
        });
        update = (Button) findViewById(R.id.update);
        update.bringToFront();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TranslinkClient().execute();
                Toast.makeText(getApplicationContext(), "updating", Toast.LENGTH_SHORT).show();
            }
        });

        setUpMapIfNeeded();
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
        mMap.setBuildingsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMarkerClickListener(new markerPressed());
    }


    private class markerPressed implements GoogleMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            translink.addBlackList(displayList.get(marker).getVehicleNo());
            marker.remove();
            return true;
        }
    }

    private void getRouteNo(){
        final EditText input = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.selected_bus);
        builder.setTitle("Bus Route Selection");
        builder.setMessage("Type your bus route (Should be integer)");
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    translink = new TranslinkParser(Integer.parseInt(input.getText().toString()));
                    routeChoice.setEnabled(false);
                     new TranslinkClient().execute();
                } catch (NumberFormatException e) {
                    e.printStackTrace();

                }
            }
        });
        builder.create().show();
    }

    private class TranslinkClient extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            displayList = new HashMap<Marker,Bus>();
            translink.get();
            myBusList = translink.buslist;
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


            mMap.clear();
            for (Bus bus : myBusList) {
                if(bus.getLatitude() != 0 && bus.getLongitude() != 0) {
                    displayList.put(mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(bus.getLatitude(), bus.getLongitude()))
                            .title(String.valueOf(bus.getVehicleNo()))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)))
                            ,bus);
                }
            }

            if (displayList.values().size() > 0) {
                LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                for (Bus bus : displayList.values()) {
                    bounds.include(new LatLng(bus.getLatitude(), bus.getLongitude()));
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50));
            }
            routeChoice.setEnabled(true);
        }

    }
}


