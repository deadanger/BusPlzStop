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
 * Created by chiang on 1/4/2015.
 */
public class StopParserUpdate extends DefaultHandler{

    public Set<Stop> getStopList() {
        return stopList;
    }

    private Set<Stop> stopList;
    private Stop stop;
    private String TAG = "StopParserUpdate";
    private String origin;
    private String data = "";
    private Boolean dataTag = false;
    private Set<Integer> blackList = new HashSet<Integer>();

    public StopParserUpdate(Bus bus){
        origin ="http://api.translink.ca/rttiapi/v1/stops?apikey=faYApdPzIJThbIF16yCP" +
                "&lat=" + bus.getLatitude() +
                "&long=" + bus.getLongitude() +
                "&radius=100" +
                "&routeNo=" + bus.getRouteNo();
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
        }else if(localName.equalsIgnoreCase("Distance")) {
            stop.setDistance(Integer.parseInt(data));
        }else if(localName.equalsIgnoreCase("Routes")) {
            stop.setRoutes(data);
        }else if(localName.equalsIgnoreCase("Stop")) {
            stopList.add(stop);
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

}
