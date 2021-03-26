package com.finals.kinoarena.Controller;

import com.finals.kinoarena.Exceptions.*;
import com.finals.kinoarena.Model.DTO.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.DateTimeException;

public class AbstractController {

//    @ExceptionHandler(MissingFieldException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorDTO handleMissingFieldException(MissingFieldException e) {
//        return new ErrorDTO(e.getMessage());
//    }
//
//    @ExceptionHandler(UserAlreadyExistsException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorDTO handleUserAlreadyExistsException(UserAlreadyExistsException e) {
//        return new ErrorDTO(e.getMessage());
//    }
//
//    @ExceptionHandler(WrongCredentialsException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ErrorDTO handleWrongCredentialsException(WrongCredentialsException e) {
//        return new ErrorDTO(e.getMessage());
//    }
//
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleUserNotFoundException(UnauthorizedException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleUserBadCredentialsException(BadRequestException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(MissingCinemasInDBException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  ErrorDTO handleMissingCinemasInDBException(MissingCinemasInDBException e){
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public  ErrorDTO NotFoundException(NotFoundException e){
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(DateTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  ErrorDTO NotAdminException(DateTimeException e){
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(AlreadyLoggedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  ErrorDTO AlreadyLoggedException(AlreadyLoggedException e){
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(CinemaAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  ErrorDTO CinemaAlreadyExistException(CinemaAlreadyExistException e){
        return new ErrorDTO(e.getMessage());
    }


}
