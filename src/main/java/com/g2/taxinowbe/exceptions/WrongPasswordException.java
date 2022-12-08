package com.g2.taxinowbe.exceptions;

public class WrongPasswordException extends Exception{

    public WrongPasswordException() {
        super("Wrong password.");
    }
}
