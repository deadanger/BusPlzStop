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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stop)) return false;

        Stop stop = (Stop) o;

        if (stopNo != stop.stopNo) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return stopNo;
    }

    private double latitude;
    private double longitude;
    private int stopNo;
    private String name;
    private String city;
    private String onStreet;
    private String atStreet;
    private String routes;
    private int distance;

}
