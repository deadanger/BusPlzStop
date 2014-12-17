
package com.example.chiang.busstop.Model;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/*
 * Created by chiang on 12/15/2014.
 */


@Root(name = "Bus")
public class Bus {
   @Element(name = "VehicleNo")
    public int vehicleNo;

   @Element(name = "RouteNo")
    public int routeNo;

    @Element(name = "Direction")
    public String direction;

    @Element(name = "Destination")
    public String destination;

    @Element(name = "Latitude")
    public double latitude;

    @Element(name = "Longitude")
    public double longitude;

    @Element(name = "RecordedTime")
    public String recordedTime;



}

