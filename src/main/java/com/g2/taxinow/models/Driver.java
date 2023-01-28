package com.g2.taxinow.models;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Class representing a taxi driver.
 */
@XmlRootElement(name = "driver")
public class Driver extends User {

    /**
     * Name of the car's model
     */
    private String carModel;

    /**
     * String of the car's plate
     */
    private String carPlate;

    /**
     * Number of the car's seats
     */
    private int numOfSeats;


    public Driver() {
    }

    public Driver(String username, String name, String surname) {
        setUsername(username);
        setName(name);
        setSurname(surname);
    }

    /**
     * Creates a new Driver object
     *
     * @param ID ID of the driver
     * @param name name of the driver
     * @param surname surname of the driver
     * @param username username of the driver
     * @param email e-mail of the driver
     * @param carModel name of the car's model
     * @param carPlate string of the car's plate
     * @param numOfSeats number of the car's seats
     */
    public Driver(String ID, String name, String surname, String username, String email,String carModel, String carPlate, int numOfSeats, String phoneNumber) {
        setID(ID);
        setUsername(username);
        setName(name);
        setSurname(surname);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setCarPlate(carPlate);
        setNumOfSeats(numOfSeats);
        setCarModel(carModel);
    }

    public String getCarModel() {
        return carModel;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public int getNumOfSeats() {
        return numOfSeats;
    }

    @XmlElement
    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    @XmlElement
    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    @XmlElement
    public void setNumOfSeats(int numOfSeats) {
        this.numOfSeats = numOfSeats;
    }

    @Override
    public String toString() {
        return "Driver{" +
                super.toString() +
                "carModel='" + carModel + '\'' +
                ", carPlate='" + carPlate + '\'' +
                ", numOfSeats=" + numOfSeats +
                '}';
    }

}
