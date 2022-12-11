package com.g2.taxinowbe.models;

import jakarta.xml.bind.annotation.*;

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

}
