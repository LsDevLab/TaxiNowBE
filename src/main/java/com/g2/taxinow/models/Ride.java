package com.g2.taxinow.models;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;


/**
 * Class representing a taxi ride.
 */
@XmlRootElement(name = "ride")
public class Ride implements Serializable {

    private static final long serialVersionUID = 1329689096267457395L;

    /**
     * ID of the ride
     */
    private String ID;
    /**
     * ID of the customer who creates the ride request
     */
    private String customerID;
    /**
     * The state of the ride (pending, accepted, completed)
     */
    private RideState state;
    /**
     * The ID of the driver who accepts the ride
     */
    private String acceptedByDriverID;
    /**
     * The number of passengers of the ride
     */
    private int numOfPassengers;
    /**
     * The date of creation of the ride
     */
    private long createdOn;
    /**
     * The max price the customer can pay in euros for the ride
     */
    private Float maxAllowedPrice;
    /**
     * The price esthablished by the driver when he accepts
     */
    private Float acceptationPrice;
    /**
     * The starting address
     */
    private String startingAddress;
    /**
     * The destination address
     */
    private String destinationAddress;

    /**
     * Creates a new Ride object, setting it as pending
     *
     * @param customerID ID of the driver who accepts the ride
     * @param numOfPassengers the number of passengers
     * @param createdOn the date of creation
     * @param maxAllowedPrice max price the customer can pay in euros for the ride
     */
    public Ride(String customerID, int numOfPassengers, long createdOn, Float maxAllowedPrice) {
        this.customerID = customerID;
        this.numOfPassengers = numOfPassengers;
        this.createdOn = createdOn;
        this.maxAllowedPrice = maxAllowedPrice;
        this.state = RideState.PENDING;
    }

    /**
     * Creates an empty Ride object
     */
    public Ride() {
    }

    /**
     * Set the ride as accepted
     *
     * @param acceptedByDriverID the ID of the driver who accepted the ride
     * @param acceptationPrice the acceptation price
     */
    public void setAccepted(String acceptedByDriverID, Float acceptationPrice) {
        this.acceptedByDriverID = acceptedByDriverID;
        this.acceptationPrice = acceptationPrice;
        this.state = RideState.ACCEPTED;
    }

    /**
     * Set the ride as not accepted (pending)
     */
    public void setPending() {
        this.acceptedByDriverID = null;
        this.acceptationPrice = null;
        this.state = RideState.PENDING;
    }

    /**
     * Set the ride as completed
     */
    public void setCompleted(){
        this.state = RideState.COMPLETED;
    }

    public RideState getState() {
        return state;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getAcceptedByDriverID() {
        return acceptedByDriverID;
    }

    public Float getAcceptationPrice() {
        return acceptationPrice;
    }

    public int getNumOfPassengers() {
        return numOfPassengers;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public Float getMaxAllowedPrice() {
        return maxAllowedPrice;
    }

    public String getID() {
        return ID;
    }

    public String getStartingAddress() {
        return startingAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    @XmlElement
    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    @XmlElement
    public void setNumOfPassengers(int numOfPassengers) {
        this.numOfPassengers = numOfPassengers;
    }

    @XmlElement
    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    @XmlElement
    public void setMaxAllowedPrice(Float maxAllowedPrice) {
        this.maxAllowedPrice = maxAllowedPrice;
    }

    @XmlElement
    public void setID(String ID) {
        this.ID = ID;
    }

    @XmlElement
    public void setStartingAddress(String startingAddress) {
        this.startingAddress = startingAddress;
    }

    @XmlElement
    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    @XmlElement
    public void setState(RideState state) {
        this.state = state;
    }

    @XmlElement
    public void setAcceptedByDriverID(String acceptedByDriverID) {
        this.acceptedByDriverID = acceptedByDriverID;
    }

    @XmlElement
    public void setAcceptationPrice(Float acceptationPrice) {
        this.acceptationPrice = acceptationPrice;
    }

    @Override
    public String toString() {
        return "Ride{" +
                "ID='" + ID + '\'' +
                ", customerID='" + customerID + '\'' +
                ", state=" + state +
                ", acceptedByDriverID='" + acceptedByDriverID + '\'' +
                ", numOfPassengers=" + numOfPassengers +
                ", createdOn=" + createdOn +
                ", maxAllowedPrice=" + maxAllowedPrice +
                ", acceptationPrice=" + acceptationPrice +
                ", startingAddress='" + startingAddress + '\'' +
                ", destinationAddress='" + destinationAddress + '\'' +
                '}';
    }

}
