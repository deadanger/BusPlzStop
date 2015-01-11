package com.example.chiang.busstop.Model;

/**
 * Created by chiang on 1/11/2015.
 */
public class Schedule {

    private String pattern;
    private String destination;
    private String time;
    private String countdown;
    private boolean cancelledTrip;
    private boolean cancelledStop;
    private String lastUpdate;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCountdown() {
        return countdown;
    }

    public void setCountdown(String countdown) {
        this.countdown = countdown;
    }

    public boolean isCancelledTrip() {
        return cancelledTrip;
    }

    public void setCancelledTrip(boolean cancelledTrip) {
        this.cancelledTrip = cancelledTrip;
    }

    public boolean isCancelledStop() {
        return cancelledStop;
    }

    public void setCancelledStop(boolean cancelledStop) {
        this.cancelledStop = cancelledStop;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
