package com.g2.taxinow.exceptions;

/**
 * An exception class representing an error to be thrown when a give username doesn't exist
 */
public class UserNotExistsException extends Exception{

    public UserNotExistsException() {
        super("User with specified username does not exist.");
    }

}
