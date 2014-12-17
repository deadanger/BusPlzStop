
package com.example.chiang.busstop.Model;

//import org.simpleframework.xml.ElementList;
//import org.simpleframework.xml.Root;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Iterator;
import java.util.List;


/**
 * Created by chiang on 12/16/2014.
 */

@Root(name = "Buses")
public class Buses implements Iterable<Bus>{

   @ElementList(name = "Bus",inline = true)
    public List<Bus> bus;

    @Override
    public Iterator<Bus> iterator() {
        return bus.iterator();
    }
}

