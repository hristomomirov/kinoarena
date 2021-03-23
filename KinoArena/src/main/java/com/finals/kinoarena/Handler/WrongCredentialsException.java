package com.finals.kinoarena.Handler;

public class WrongCredentialsException extends Exception {

    private String message;

    public WrongCredentialsException(String message) {
        this.message = message;
    }
}
