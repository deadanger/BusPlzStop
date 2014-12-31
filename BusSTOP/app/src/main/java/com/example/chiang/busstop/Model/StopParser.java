package com.example.chiang.busstop.Model;

import android.util.Log;

import com.example.chiang.busstop.MapsActivity;
import com.google.android.gms.maps.model.LatLng;

import org.simpleframework.xml.Default;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by chiang on 12/24/2014.
 */
public class StopParser extends DefaultHandler{

    public ArrayList<Stop> getStopList() {
        return stopList;
    }

    private ArrayList<Stop> stopList;
    private Stop stop;
    private String TAG = "StopParser";
    private String origin;
    private String data = "";
    private Boolean dataTag = false;
    private Set<Integer> blackList = new HashSet<Integer>();

    public StopParser(LatLng loc, int busRoute){
        origin ="http://api.translink.ca/rttiapi/v1/stops?" +
                "apikey=faYApdPzIJThbIF16yCP&lat=" + loc.latitude +
                "&long=" + loc.longitude +
                "&routeNo=" + busRoute +
                "&radius=2000";
        get();
    }

    private void get(){
        try {
            stopList = new ArrayList<Stop>();
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
        if(localName.equals("Stop"))
            stop = new Stop();

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        dataTag = false;

        if(localName.equalsIgnoreCase("StopNo")) {
            stop.setStopNo(Integer.parseInt(data));
        }else if(localName.equalsIgnoreCase("Name")) {
            stop.setName(data);
        }else if(localName.equalsIgnoreCase("City")) {
            stop.setCity(data);
        }else if(localName.equalsIgnoreCase("OnStreet")) {
            stop.setOnStreet(data);
        }else if(localName.equalsIgnoreCase("AtStreet")) {
            stop.setAtStreet(data);
        }else if(localName.equalsIgnoreCase("Latitude")) {
            stop.setLatitude(Double.parseDouble(data));
        }else if(localName.equalsIgnoreCase("Longitude")) {
            stop.setLongitude(Double.parseDouble(data));
        }else if(localName.equalsIgnoreCase("Routes")) {
            stop.setRoutes(data);
        } else if (localName.equals("Stop")){
            if(!blackList.contains(stop.getStopNo())) {
                stopList.add(stop);
            }
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

    //TODO
    public void selectStop(int selection){



    }

}
