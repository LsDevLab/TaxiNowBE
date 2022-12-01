package com.g2.taxinowbe.models;

public class Customer {

    private String email, name, surname, hashedPassword;

    public Customer() {
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                '}';
    }
}
