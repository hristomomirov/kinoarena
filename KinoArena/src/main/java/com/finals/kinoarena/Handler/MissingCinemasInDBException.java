package com.finals.kinoarena.Handler;

public class MissingCinemasInDBException extends Exception {
    private String message;

    public MissingCinemasInDBException(String message) {
        this.message =  message;

    }
}
