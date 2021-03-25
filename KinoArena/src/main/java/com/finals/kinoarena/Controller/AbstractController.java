package com.finals.kinoarena.Controller;

import com.finals.kinoarena.Exceptions.*;
import com.finals.kinoarena.Model.DTO.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

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
//    @ExceptionHandler(UserNotFoundException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorDTO handleUserNotFoundException(UserNotFoundException e) {
//        return new ErrorDTO(e.getMessage());
//    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleUserBadCredentialsException(BadRequestException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(MissingCinemasInDBException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  ErrorDTO handleMissingCinemasInDBExceptopn(MissingCinemasInDBException e){
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  ErrorDTO NotFoundException(NotFoundException e){
        return new ErrorDTO(e.getMessage());
    }

////    @ExceptionHandler(NotAdminException.class)
////    @ResponseStatus(HttpStatus.UNAUTHORIZED)
////    public  ErrorDTO NotAdminException(NotAdminException e){
////        return new ErrorDTO(e.getMessage());
//    }

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
