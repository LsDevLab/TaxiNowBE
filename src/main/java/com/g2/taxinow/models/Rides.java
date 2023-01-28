package com.g2.taxinow.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "rides")
@XmlAccessorType(XmlAccessType.FIELD)
public class Rides {

    @XmlElement(name="ride")
    private List<Ride> rides;

    public Rides() {
    }

    public Rides(List<Ride> rides) {
        this.rides = rides;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }

    @Override
    public String toString() {
        String str = "Rides{" +
                "rides=\n";
        for (Ride ride: rides){
            str +=  "\t" + ride + ", \n";
        }
        str += '}';
        return str;
    }
}
