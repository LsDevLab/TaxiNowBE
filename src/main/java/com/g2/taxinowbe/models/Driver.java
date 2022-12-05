package com.g2.taxinowbe.models;

/**
 * Class representing a taxi driver.
 */

public class Driver {
    /**
     * Name of the taxi driver
     */
    private  String name;

    /**
     * Surname of the taxi driver
     */
    private  String surname;

    /**
     * Email of the taxi driver
     */
    private  String email;

    /**
     * ID of the taxi driver
     */
    private  String driverID;

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
    private int numOfSeats;    /**
     * Creates a new Customer object
     *
     * @param driverID ID of the driver
     * @param name name of the driver
     * @param surname surname of the driver
     * @param email e-mail of the driver
     * @param carModel name of the car's model
     * @param carPlate string of the car's plate
     * @param numOfSeats number of the car's seats
     */

    public Driver(String name, String surname, String email, String hashedPassword, String driverID,String carModel, String carPlate, int numOfSeats) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.driverID = driverID;
        this.carModel = carModel;
        this.carPlate = carPlate;
        this.numOfSeats = numOfSeats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public int getNumOfSeats() {
        return numOfSeats;
    }

    public void setNumOfSeats(int numOfSeats) {
        this.numOfSeats = numOfSeats;
    }

    @Override
    public String toString() {
        return "Driver " + driverID + ": " + name + " " + surname + ", " + email+ " ;Car"  + carModel + ": "+ carPlate + ", " + numOfSeats ;
    }
}
