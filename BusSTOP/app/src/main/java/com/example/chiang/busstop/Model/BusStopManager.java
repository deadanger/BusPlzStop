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
    private Stop selectedStop;


    public BusStopManager(){
        stopSet = new HashSet<Stop>();
    }

    public void parse(Set<Integer> stopNoList){
            stopSet.clear();
        if(selectedStop == null) {
            for (Integer stopID : stopNoList) {
                parseHelper(stopID);
            }
            stopSet.remove(null);
        } else{
            stopSet.clear();
            stopSet.add(selectedStop);
        }
    }

    private void parseHelper(int stopNo){
            parser = new StopParser(stopNo);
            stopSet.add(parser.getStop());
    }


    public Set<Stop> getStops() {
        return stopSet;
    }



    // return true if the bus has arrived to selected stop
    public boolean isArrive(Bus bus){
        updateParser = new StopParserUpdate(bus);

        if(selectedStop != null) {
            return (selectedStop.equals(updateParser.getStop()));
        }
        return false;
    }

    public void filterBusStops(Bus bus){

    }

    public Stop getSelectedStop() {
        return selectedStop;
    }

    public void setSelectedStop(Stop selectedStop) {
        this.selectedStop = selectedStop;
    }

    public void reset(){
        selectedStop = null;
    }




}
