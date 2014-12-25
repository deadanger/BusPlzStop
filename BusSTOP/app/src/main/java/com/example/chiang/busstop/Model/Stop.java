package com.example.chiang.busstop.Model;

/**
 * Created by chiang on 12/24/2014.
 */
public class Stop {

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getStopNo() {
        return stopNo;
    }

    public void setStopNo(int stopNo) {
        this.stopNo = stopNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOnStreet() {
        return onStreet;
    }

    public void setOnStreet(String onStreet) {
        this.onStreet = onStreet;
    }

    public String getAtStreet() {
        return atStreet;
    }

    public void setAtStreet(String atStreet) {
        this.atStreet = atStreet;
    }

    public String getRoutes() {
        return routes;
    }

    public void setRoutes(String routes) {
        this.routes = routes;
    }

    private double latitude;
    private double longitude;
    private int stopNo;
    private String name;
    private String city;
    private String onStreet;
    private String atStreet;
    private String routes;


}
