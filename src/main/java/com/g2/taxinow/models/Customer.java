package com.g2.taxinow.models;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Class representing a taxi customer.
 */
@XmlRootElement(name = "customer")
public class Customer extends User{

    /**
     * Creates an empty Customer object
     */
    public Customer() {
    }

    public Customer(String username, String name, String surname) {
        setUsername(username);
        setName(name);
        setSurname(surname);
    }

    /**
     * Creates a new Customer object
     *
     * @param ID ID of the customer
     * @param name name of the customer
     * @param surname surname of the customer
     * @param email e-mail of the customer
     * @param phoneNumber phone number of the customer
     */
    public Customer(String ID, String username, String name, String surname, String email, String phoneNumber) {
        setID(ID);
        setUsername(username);
        setName(name);
        setSurname(surname);
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }

    @Override
    public String toString() {
        return "Customer " + getID() + ": " + getName() + " " + getSurname() + ", " + getEmail() + ", " + getPhoneNumber();
    }

}
