package com.g2.taxinow.notifier;

import com.g2.taxinow.models.Ride;

import java.io.Serializable;

/**
 * A class representing a ride notification, to be sent by the notifier
 */
public class RideNotification implements Serializable {

    // field used for correct serialization between two different applications
    private static final long serialVersionUID = 6529685098267757690L;

    /**
     * Types for notification
     */
    public enum Type{

        /**
         * A new ride is created
         */
        NEW,

        /**
         * An existing ride is edited
         */
        EDITED,

        /**
         * An existing ride is deleted
         */
        DELETED
    };

    /**
     * The ride involved in the notification
     */
    private Ride ride;

    /**
     * The type of the ride
     */
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
