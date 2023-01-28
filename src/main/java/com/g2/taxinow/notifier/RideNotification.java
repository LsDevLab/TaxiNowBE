package com.g2.taxinow.notifier;

import com.g2.taxinow.models.Ride;

import java.io.Serializable;

public class RideNotification implements Serializable {

    private static final long serialVersionUID = 6529685098267757690L;

    public enum Type{
        NEW,
        EDITED,
        DELETED
    };

    private Ride ride;
    private Type type;

    public RideNotification(Type type, Ride ride) {
        this.type = type;
        this.ride = ride;
    }

    public Ride getRide() {
        return ride;
    }

    public Type getType() {
        return type;
    }


}
