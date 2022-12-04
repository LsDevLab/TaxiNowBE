package com.g2.taxinowbe.models;

/**
 * Class representing a taxi customer.
 */
public class Customer {

    /**
     * ID of the customer
     */
    private String customerID;
    /**
     * Name of the customer
     */
    private String name;
    /**
     * Surname of the customer
     */
    private String surname;
    /**
     * E-mail of the customer
     */
    private String email;
    /**
     * Phone number of the cutomer
     */
    private String phoneNumber;

    /**
     * Creates a new Customer object
     *
     * @param customerID ID of the customer
     * @param name name of the customer
     * @param surname surname of the customer
     * @param email e-mail of the customer
     * @param phoneNumber phone number of the customer
     */
    public Customer(String customerID, String name, String surname, String email, String phoneNumber) {
        this.customerID = customerID;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Customer " + customerID + ": " + name + " " + surname + ", " + email + ", " + surname;
    }
}
