package com.finals.kinoarena.Handler;

public class MissingFieldException extends Exception {

    private String message;
    public MissingFieldException(String message) {
        this.message = message;
    }
}
