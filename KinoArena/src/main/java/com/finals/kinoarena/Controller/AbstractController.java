package com.finals.kinoarena.Controller;

import com.finals.kinoarena.Exceptions.*;
import com.finals.kinoarena.Model.DTO.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class AbstractController {

    @ExceptionHandler(MissingFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMissingFieldException(MissingFieldException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(WrongCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleWrongCredentialsException(WrongCredentialsException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleUserNotFoundException(UserNotFoundException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleUserBadCredentialsException(BadCredentialsException e) {
        return new ErrorDTO(e.getMessage());
    }
}
