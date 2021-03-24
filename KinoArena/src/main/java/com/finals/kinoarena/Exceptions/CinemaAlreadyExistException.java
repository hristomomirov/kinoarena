package com.finals.kinoarena.Exceptions;

import com.finals.kinoarena.Model.Entity.Cinema;

public class CinemaAlreadyExistException extends Exception{
    public CinemaAlreadyExistException(String msg){
        super(msg);
    }
}
