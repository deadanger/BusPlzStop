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
    private StopParserUpdate updateParser;
    private final String TAG = "BusStopManager";
    private Set<Stop> deletedStops;
    private Stop selectedStop;


    public BusStopManager(){
        stopSet = new HashSet<Stop>();
        deletedStops = new HashSet<Stop>();
    }

    public void parse(Set<Integer> stopNoList){
        for(Integer stopID: stopNoList){
            parseHelper(stopID);
        }
       stopSet.remove(null);
    }

    private void parseHelper(int stopNo){

            parser = new StopParser(stopNo);
            stopSet.add(parser.getStop());
    }


    public Set<Stop> getStops() {
        return stopSet;
    }



    // return true if the bus has arrived to a stop or passed a stop
    public boolean update(Bus bus){
        updateParser = new StopParserUpdate(bus);
        Set<Stop> removingSet = updateParser.getStopList();
        if(removingSet != null) {
            if (stopSet.removeAll(updateParser.getStopList())) {
                deletedStops.addAll(updateParser.getStopList());
                return true;
            }
        }
        return false;
    }

    public Stop getSelectedStop() {
        return selectedStop;
    }

    public void setSelectedStop(Stop selectedStop) {
        this.selectedStop = selectedStop;
    }

    public void resetStops(){
        deletedStops.clear();
    }


}
