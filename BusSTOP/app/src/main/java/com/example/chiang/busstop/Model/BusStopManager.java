package com.example.chiang.busstop.Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by chiang on 12/31/2014.
 */
public class BusStopManager {

    private Set<Stop> stopSet;
    private StopParser parser;
    final private double r = 0.052872;
    private int counter = 3;
    public BusStopManager(List<LatLng> locations, int routeNo){
        parse(locations,routeNo);
    }

    private void parse(List<LatLng> locations, int routeNo){
        List<Stop> stops = new ArrayList<Stop>();
        for(LatLng loc: locations) {
            stops.addAll(parseHelper(loc, routeNo, counter));
        }
        stopSet = new HashSet<Stop>(stops);
    }

    private List<Stop> parseHelper(LatLng location, int routeNo, int count){
        if(count == 0){
            return new ArrayList<Stop>();
        }

        List<Stop> stops = new ArrayList<Stop>();

        double lat = location.latitude;
        double lon = location.longitude;

        LatLng north = new LatLng(lat + r, lon);
        LatLng south = new LatLng(lat - r, lon);
        LatLng west = new LatLng(lat, lon - r);
        LatLng east = new LatLng(lat, lon + r);

        stops.addAll(parseHelper(location, routeNo, count--));
        stops.addAll(parseHelper(north, routeNo, count--));
        stops.addAll(parseHelper(south, routeNo, count--));
        stops.addAll(parseHelper(west, routeNo, count--));
        stops.addAll(parseHelper(east, routeNo, count--));
        return stops;
    }

    public Set<Stop> getStops() {
        return stopSet;
    }
}
