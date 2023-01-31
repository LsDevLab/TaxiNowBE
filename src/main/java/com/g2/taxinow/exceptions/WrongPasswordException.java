package com.g2.taxinow.exceptions;

/**
 * An exception class representing an error to be thrown when the password for a user is wrong.
 */
public class WrongPasswordException extends Exception{

    public WrongPasswordException() {
        super("Wrong password.");
    }
}
