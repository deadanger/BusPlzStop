package com.example.chiang.busstop;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.chiang.busstop.Model.ApiService;
import com.example.chiang.busstop.Model.Bus;
import com.example.chiang.busstop.Model.Buses;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobprofs.retrofit.converters.SimpleXmlConverter;


import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Buses myBus;
    private String apikey = "faYApdPzIJThbIF16yCP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

        //Default Marker
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("myplace"));


        Data myData = new Data();
        Log.i("Step", "start execute");

        myData.drawMarker(myData.getBuses(99));
        Log.i("Step", "end execute");
    }


    private class Data{

        public  Buses getBuses(int routeNo) {
            Log.i("Step", "start doInBackground");
            Buses busList;
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.translink.ca/rttiapi/v1")
                    .setConverter(new SimpleXmlConverter())
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            ApiService apiService = restAdapter.create(ApiService.class);

            try {
                busList = apiService.getBus(apikey, 99);
                return busList;
            } catch (RetrofitError e) {
                System.out.println(e.getResponse().getStatus());
            }
                return new Buses();
        }

        public void drawMarker(Buses lob){
            Log.i("Step", "onPostExecute");
            for(Bus bus: lob){
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(bus.latitude, bus.longitude))
                        .title(bus.direction));
            }
        }

    }

}
