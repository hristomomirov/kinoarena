package com.finals.kinoarena.Handler;

public class UserAlreadyExistsException extends Exception {

    private String message;

    public UserAlreadyExistsException(String message) {
        this.message =  message;

    }
}
