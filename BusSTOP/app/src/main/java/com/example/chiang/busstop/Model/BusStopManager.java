package com.example.chiang.busstop.Model;

import android.text.style.TabStopSpan;
import android.util.Log;

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
    private final String TAG = "BusStopManager";


    public BusStopManager(Set<Integer> stopNoList){
        stopSet = new HashSet<Stop>();
        parse(stopNoList);
    }

    private void parse(Set<Integer> stopNoList){
        for(Integer stopID: stopNoList){
            parseHelper(stopID);
        }
    }

    private void parseHelper(int stopNo){

            parser = new StopParser(stopNo);
            stopSet.add(parser.getStop());
    }


    public Set<Stop> getStops() {
        return stopSet;
    }


}
