package com.example.chiang.busstop.Model;

import android.content.res.AssetManager;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by chiang on 1/2/2015.
 */
public class RouteStopParser extends DefaultHandler{
    private Map<String, Set<Integer>> routeToStopID;
    private Set<Integer> stopNoList;
    private String routeNo;
    private String TAG = "RouteStopParser";
    private String data = "";
    private Boolean dataTag = false;

    public RouteStopParser(AssetManager am){
        init(am);
    }

    private void init(AssetManager am){
        try {
            AssetManager assetManager = am;
            InputStream input;
            input = am.open("BusStopsXML.txt");
            stopNoList = new HashSet<Integer>();
            routeToStopID = new HashMap<String, Set<Integer>>();
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            xmlReader.setContentHandler(this);
            xmlReader.parse(new InputSource(new InputStreamReader(input)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        dataTag = true;
        data = "";
        if(localName.equals("Route")){
            routeNo = "";
            stopNoList = new HashSet<Integer>();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        dataTag = false;

        if(localName.equalsIgnoreCase("RouteNo")) {
            routeNo = data;
        } else if (localName.equalsIgnoreCase("Stop")){
            if(data != "") {
                stopNoList.add(Integer.parseInt(data));
            }
        } else if (localName.equals("Route")){
            routeToStopID.put(routeNo, stopNoList);
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

    public Map<String, Set<Integer>> getRouteToStopID() {
        return routeToStopID;
    }
}
