package com.example.chiang.busstop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chiang.busstop.Model.Bus;
import com.example.chiang.busstop.Model.BusParser;
import com.example.chiang.busstop.Model.BusStopManager;
import com.example.chiang.busstop.Model.Stop;
import com.example.chiang.busstop.Model.StopParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

    private Map<Marker,Bus> displayList;
    private Map<Marker, Stop> stopList = new HashMap<Marker, Stop>();
    private BusParser translink;
    private Button routeChoice;
    private Button update;
    private BusStopManager stopManager;
    private int routeNo;
    private boolean resetStop = true;
    private Set<Stop> myBusStopList;

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
            if(displayList.get(marker) != null) {
                if(displayList.values().size() > 1){
                    translink.selectBus(displayList.get(marker).getVehicleNo());

                }else{
                    translink.resetBus();
                }
                new TranslinkClient().execute();
            } else if (stopList.get(marker) != null) {
                stopList.remove(marker);
                marker.remove();
            }
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
        resetStop = false;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    routeNo = Integer.parseInt(input.getText().toString());
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
        private List<Bus> myBusList;


        @Override
        protected Object doInBackground(Object[] params) {

            displayList = new HashMap<Marker,Bus>();
            stopList = new HashMap<Marker, Stop>();
            translink = new BusParser(routeNo);
            myBusList = translink.buslist;

            Iterator<Bus> it = myBusList.iterator();
            while(it.hasNext()){
                Bus bus = it.next();
                if(bus.getLatitude() == 0 && bus.getLongitude() == 0){
                    it.remove();
                }
            }

            if(resetStop) {
                if(false) {
                    List<LatLng> los = new ArrayList<LatLng>();

                    for (Bus bus : myBusList) {
                        los.add(new LatLng(bus.getLatitude(), bus.getLongitude()));
                    }
                    stopManager = new BusStopManager(los, routeNo);
                } else {
                    Bus bus = myBusList.get(0);
                    LatLng los = new LatLng(bus.getLatitude(),bus.getLongitude());
                    stopManager = new BusStopManager(los, routeNo);
                }



                myBusStopList = stopManager.getStops();
                resetStop = false;
            } else{

                myBusStopList = new HashSet<Stop>();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


            mMap.clear();
            for (Stop stop : myBusStopList) {
                    stopList.put(mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(stop.getLatitude(), stop.getLongitude()))
                            .title(String.valueOf(stop.getStopNo()))
                            .flat(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.stop)))
                            ,stop);

            }
            if(myBusList.size() != 0){
            for (Bus bus : myBusList) {
                displayList.put(mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(bus.getLatitude(), bus.getLongitude()))
                        .title(String.valueOf(bus.getVehicleNo()))
                        .flat(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)))
                        ,bus);

            }} else {
                Toast.makeText(getApplicationContext(), "No bus", Toast.LENGTH_SHORT).show();
            }

            if (displayList.values().size() > 0) {
                LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                for (Bus bus : displayList.values()) {
                    bounds.include(new LatLng(bus.getLatitude(), bus.getLongitude()));
                }
                for (Stop stop : stopList.values()) {
                    bounds.include(new LatLng(stop.getLatitude(), stop.getLongitude()));
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50));
            }
            routeChoice.setEnabled(true);
        }

    }
}


