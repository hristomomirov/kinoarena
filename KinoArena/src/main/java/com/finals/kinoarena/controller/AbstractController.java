package com.finals.kinoarena.controller;

import com.finals.kinoarena.util.SessionManager;
import com.finals.kinoarena.util.exceptions.BadGetawayException;
import com.finals.kinoarena.util.exceptions.BadRequestException;
import com.finals.kinoarena.util.exceptions.NotFoundException;
import com.finals.kinoarena.util.exceptions.UnauthorizedException;
import com.finals.kinoarena.model.DTO.ErrorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.FileNotFoundException;
import java.time.DateTimeException;

public class AbstractController {

    @Autowired
    protected SessionManager sessionManager;

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleUserBadCredentialsException(BadRequestException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO NotFoundException(NotFoundException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(DateTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO NotAdminException(DateTimeException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleUserNotFoundException(UnauthorizedException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ErrorDTO(e.getAllErrors().get(0).getDefaultMessage());
    }
    @ExceptionHandler(BadGetawayException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorDTO handleBadGetawayException(BadGetawayException e) {
        return new ErrorDTO(e.getMessage());
    }
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleFileNotFoundException(FileNotFoundException e) {
        return new ErrorDTO(e.getMessage());
    }
}
