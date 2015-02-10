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


    public void parse(Set<Integer> stopNoList, Bus bus){
        stopSet.clear();
        if(selectedStop == null) {
            for (Integer stopID : stopNoList) {
                parseHelper(stopID);
            }
            stopSet.remove(null);
            filterBusStops(bus);
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
        return isSelectedStopNearBus(bus);
    }

    // maintain all stops to be in same direction as the bus
    public void filterBusStops(Bus bus){
        for(Stop stop : stopSet) {
            StopEstimateManager manager = new StopEstimateManager(
                    stop.getStopNo(), bus.getRouteNo());
            StopEstimate est = manager.getStopEstimate();
            if(!est.getDirection().equalsIgnoreCase(bus.getDirection())){
                 stopSet.remove(stop);
                Log.i(TAG, "bus stop removed");
            }
        }
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

    private boolean isSelectedStopNearBus(Bus bus){
        if(hasSelectedStop()){
            return isAnyStopsSelected(getStopsNearBus(bus));
        }
        return false;
    }

    private boolean isAnyStopsSelected(Set<Stop> stops){
        for(Stop stop: stops){
            if(stop.getStopNo() == selectedStop.getStopNo()){
                return true;
            }
        }
        return false;
    }


    private Set<Stop> getStopsNearBus(Bus bus){
        return  new StopParserUpdate(bus).getStops();
    }

    public boolean hasSelectedStop(){
        return selectedStop != null;
    }


    private class StopEstimateManager{

        private StopEstimateParser parser;
        private StopEstimate stopEstimate;

        public StopEstimateManager(int stopNo, String routeNo){
            parser = new StopEstimateParser(stopNo, routeNo);
        }

        private void get(){
            stopEstimate = parser.getStopEstimate();
        }

        public StopEstimate getStopEstimate(){
            return stopEstimate;
        }


    }

}
