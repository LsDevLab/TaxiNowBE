package com.g2.taxinow.exceptions;

public class UserNotExistsException extends Exception{

    public UserNotExistsException() {
        super("User with specified username does not exist.");
    }
}
