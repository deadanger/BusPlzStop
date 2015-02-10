package com.example.chiang.busstop.Model;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by chiang on 1/11/2015.
 */
public class StopEstimateParser extends DefaultHandler {

    private StopEstimate stopEstimate;
    private String TAG = "StopEstimateParser";
    private String origin;
    private String data = "";
    private Boolean dataTag = false;
    private Set<Schedule> scheduleSet;
    private Schedule schedule;

    public StopEstimateParser(int stopNo, String routeNo){
        origin ="http://api.translink.ca/rttiapi/v1/stops/" + stopNo +
                "/estimates?apikey=faYApdPzIJThbIF16yCP&count=3&timeframe=120&routeNo ="
        + routeNo;
        get();
    }

    private void get(){
        try {
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            xmlReader.setContentHandler(this);
            xmlReader.parse(new InputSource(new URL(origin).openStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        dataTag = true;
        data = "";
        if(localName.equals("Schedules")){
            scheduleSet = new HashSet<Schedule>();
        } else if (localName.equals("Schedule")){
            schedule = new Schedule();
        } else if (localName.equals("NextBus")) {
            stopEstimate = new StopEstimate();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        dataTag = false;

        if(localName.equalsIgnoreCase("RouteNo")) {
            stopEstimate.setRouteNo(data);
        }else if(localName.equalsIgnoreCase("RouteName")) {
            stopEstimate.setRouteName(data);
        }else if(localName.equalsIgnoreCase("Direction")) {
            stopEstimate.setDirection(data);
        }else if(localName.equalsIgnoreCase("Schedules")) {
            stopEstimate.setScheduleSet(scheduleSet);
        }else if(localName.equalsIgnoreCase("Schedule")) {
           scheduleSet.add(schedule);
        }else if(localName.equalsIgnoreCase("Pattern")) {
            schedule.setPattern(data);
        }else if(localName.equalsIgnoreCase("Destination")) {
           schedule.setDestination(data);
        }else if(localName.equalsIgnoreCase("ExpectedLeaveTime")) {
            schedule.setTime(data);
        }else if(localName.equalsIgnoreCase("ExpectedCountdown")) {
            schedule.setCountdown(data);
        }else if(localName.equalsIgnoreCase("CancelledTrip")) {
            schedule.setCancelledTrip(Boolean.parseBoolean(data));
        }else if(localName.equalsIgnoreCase("CancelledStop")) {
            schedule.setCancelledStop(Boolean.parseBoolean(data));
        }else if(localName.equalsIgnoreCase("LastUpdate")) {
            schedule.setLastUpdate(data);
        }
    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if(dataTag) {
            data = new String(ch, start, length);
            dataTag = false;
        }
    }

    public StopEstimate getStopEstimate() {
        return stopEstimate;
    }
}
