package com.g2.taxinow.models;

import jakarta.xml.bind.annotation.XmlElement;

public abstract class User {

    /**
     * The ID of the user
     */
    private String ID;

    /**
     * The username of the user
     */
    private String username;

    /**
     * Name of the user
     */
    private String name;

    /**
     * Surname of the user
     */
    private String surname;

    /**
     * E-mail of the user
     */
    private String email;

    /**
     * Phone number of the user
     */
    private String phoneNumber;

    /**
     * Avatar URL of the user
     */
    private String avatarURL;

    public String getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    @XmlElement
    public void setID(String ID) {
        this.ID = ID;
    }

    @XmlElement
    public void setUsername(String username) {
        this.username = username;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @XmlElement
    public void setEmail(String email) {
        this.email = email;
    }

    @XmlElement
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @XmlElement
    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID='" + ID + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

}
