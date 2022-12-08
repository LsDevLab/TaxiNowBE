package com.g2.taxinowbe.exceptions;

public class UserNotExistsException extends Exception{

    public UserNotExistsException() {
        super("User with specified username does not exist.");
    }
}
