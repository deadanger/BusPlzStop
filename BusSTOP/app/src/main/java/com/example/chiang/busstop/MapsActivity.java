package com.example.chiang.busstop;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chiang.busstop.Model.Bus;
import com.example.chiang.busstop.Model.BusParser;
import com.example.chiang.busstop.Model.BusStopManager;
import com.example.chiang.busstop.Model.RouteStopParser;
import com.example.chiang.busstop.Model.Stop;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private Map<Marker,Bus> busMap;
    private Map<Marker,Stop> stopMap;
    private BusParser translink;
    private Button routeChoice;
    private Button update;
    private BusStopManager stopManager;
    private String routeNo;
    private Map<String, Set<Integer>> routeToStopID;
    private boolean reset;
    private boolean busSelected;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

    }

    private void getRouteStops(){
        RouteStopParser parser = new RouteStopParser(getAssets());
        routeToStopID = parser.getRouteToStopID();
        Log.i("236 getRouteStops", ""+ routeToStopID.keySet().size());
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
        mMap.setBuildingsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMarkerClickListener(new markerPressed());
        translink = new BusParser();
        stopManager = new BusStopManager();
        getRouteStops();
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
                routeChoice.setEnabled(false);
                update.setEnabled(false);
                new TranslinkClient().execute();
                Toast.makeText(getApplicationContext(), "updating", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class markerPressed implements GoogleMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            if(busMap.get(marker) != null) {
                if(busMap.values().size() > 1){
                    translink.setSelectedBus(busMap.get(marker));
                    busSelected = true;
                    Toast.makeText(getApplicationContext(), "bus selected", Toast.LENGTH_SHORT).show();
                }else{
                    busSelected = false;
                    translink.resetBus();
                }
                new TranslinkClient().execute();
            } else {
                reset = true;
                if(stopMap.values().size() > 1){
                    stopManager.setSelectedStop(stopMap.get(marker));
                    Toast.makeText(getApplicationContext(), "stop selected", Toast.LENGTH_SHORT).show();
                }else{
                    stopManager.reset();
                }
                new TranslinkClient().execute();
            }
            return true;
        }
    }

    private void getRouteNo(){
        final EditText input = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.selected_bus);
        builder.setTitle("Bus Route Selection");
        builder.setMessage("Type your bus route");
        builder.setView(input);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    routeNo = input.getText().toString();
                    routeChoice.setEnabled(false);
                    update.setEnabled(false);
                    if(checkValidRoute(routeNo)) {
                        reset = true;
                        translink.resetBus();
                        stopManager.reset();
                        new TranslinkClient().execute();
                    } else{
                        Toast.makeText(getApplicationContext(), "route Invalid", Toast.LENGTH_SHORT).show();
                        getRouteNo();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();

                }
            }
        });
        builder.create().show();
    }

    private boolean checkValidRoute(String string){
        String[] datas = getResources().getStringArray(R.array.routeOptions);
        final List<String> options = Arrays.asList(datas);

        fixRouteNo(string);
        return options.contains(routeNo);
    }

    private void fixRouteNo(String string){
        if(isFirstLetterNumber(string)){
            routeNo = makeThreeNumber(string);
        }
    }

    private String makeThreeNumber(String userInput){
        if(userInput.length() == 1){ userInput = "00".concat(userInput);}
        else if(userInput.length() == 2){ userInput = "0".concat(userInput);}

        return userInput;
    }

    private boolean isFirstLetterNumber(String userInput){
        char c = userInput.charAt(0);
        return (c >= '0' && c <= '9');
    }


    private class TranslinkClient extends AsyncTask {
        private List<Bus> myBusList;
        Set<Stop> stopList;

        @Override
        protected Object doInBackground(Object[] params) {

            busMap = new HashMap<Marker,Bus>();

            translink.parse(routeNo);
            myBusList = translink.getBuslist();

            if(reset){ // pick route
                stopMap = new HashMap<Marker, Stop>();
                if(translink.getSelectedBus() != null){
                    stopManager.parse(routeToStopID.get(routeNo), translink.getSelectedBus());
                } else {
                    stopManager.parse(routeToStopID.get(routeNo));
                }
                reset = false;
            } else{ // update
                if(translink.getSelectedBus() != null) {
                    if(hasArrived()){
                        Log.i("detector", "bus arrived");
                        notifyUser();

                    }
                }
            }
            stopList = stopManager.getStops();
            stopList.remove(null);

            return null;
        }

        private boolean hasSelectedStop(){return stopManager.hasSelectedStop();};
        private boolean hasSelectedBus(){return busSelected;};
        private boolean hasArrived(){
            if(hasSelectedStop() && hasSelectedBus()){
                return stopManager.isArrive(translink.getSelectedBus());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            mMap.clear();

            if (stopList.size() != 0) {
                for (Stop stop : stopList) {
                    stopMap.put(mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(stop.getLatitude(), stop.getLongitude()))
                                    .title(String.valueOf(stop.getStopNo()))
                                    .flat(true)
                                    .anchor(0.5f,0.5f)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.stop))),
                            stop);
                }
            }

            if (myBusList.size() != 0) {
                for (Bus bus : myBusList) {
                    if (bus.getLatitude() != 0 && bus.getLongitude() != 0) {
                        busMap.put(mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(bus.getLatitude(), bus.getLongitude()))
                                .title(String.valueOf(bus.getVehicleNo()))
                                .flat(true)
                                .anchor(0.5f,0.5f)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)))
                                , bus);
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "No bus", Toast.LENGTH_LONG).show();
            }

            if (busMap.values().size() > 0 || stopList.size() > 0) {
                LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                for (Bus bus : busMap.values()) {
                    bounds.include(new LatLng(bus.getLatitude(), bus.getLongitude()));
                    if (busSelected) {
                        Circle circle = mMap.addCircle(new CircleOptions()
                                .center(new LatLng(bus.getLatitude(), bus.getLongitude()))
                                .radius(500)
                                .strokeWidth(2)
                                .strokeColor(Color.BLUE)
                                .fillColor(Color.parseColor("#500084d3")));
                    }
                }
                for (Stop stop : stopList) {
                    bounds.include(new LatLng(stop.getLatitude(), stop.getLongitude()));
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 30));
            }


            routeChoice.setEnabled(true);
            update.setEnabled(true);
        }
        private void notifyUser() {
            int ID = 0;
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(ID, getNotification());
        }
        private Notification getNotification() {
            Notification.Builder notificationSetting;
            notificationSetting = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Bus Notification")
                    .setContentText("almost there. Be prepared!")
                    .setLights(Color.RED, 3000, 3000)
                    .setSmallIcon(R.drawable.selected_bus)
                    .setVibrate(new long[]{1000,1000,1000,1000});

            return notificationSetting.build();
        }



    }
}


