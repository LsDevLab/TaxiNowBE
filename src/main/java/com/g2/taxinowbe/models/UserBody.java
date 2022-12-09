package com.g2.taxinowbe.models;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "user")
public class UserBody implements Serializable {

    /**
     * The username of the user
     */
    public String username;
    /**
     * Name of the user
     */
    public String name;
    /**
     * Surname of the user
     */
    public String surname;
    /**
     * Hashed password of the user
     */
    public String hashedPassword;

    public UserBody() {
    }

    public UserBody(String username, String name, String surname, String hashedPassword) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.hashedPassword = hashedPassword;
    }

}
