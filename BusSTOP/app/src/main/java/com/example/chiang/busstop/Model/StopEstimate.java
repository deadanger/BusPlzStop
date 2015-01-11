package com.example.chiang.busstop.Model;

import java.util.Set;

/**
 * Created by chiang on 1/11/2015.
 */
public class StopEstimate {
    private String routeNo;
    private String routeName;
    private String direction;
    private Set<Schedule> scheduleSet;

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Set<Schedule> getScheduleSet() {
        return scheduleSet;
    }

    public void setScheduleSet(Set<Schedule> scheduleSet) {
        this.scheduleSet = scheduleSet;
    }
}
