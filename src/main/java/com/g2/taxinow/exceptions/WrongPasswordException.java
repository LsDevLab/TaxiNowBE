package com.g2.taxinow.exceptions;

public class WrongPasswordException extends Exception{

    public WrongPasswordException() {
        super("Wrong password.");
    }
}
