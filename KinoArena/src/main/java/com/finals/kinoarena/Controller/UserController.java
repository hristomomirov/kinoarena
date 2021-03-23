package com.finals.kinoarena.Controller;

import com.finals.kinoarena.DAO.UserDao;
import com.finals.kinoarena.DTO.UserDTO;
import com.finals.kinoarena.Handler.*;
import com.finals.kinoarena.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@RestController
public class UserController {

    @Autowired
    private UserDao dao;

    @PostMapping(value = "/user/register")
    public User registerNewUser(@RequestBody UserDTO userDTO) throws UserAlreadyExistsException, MissingFieldException {
        if (validateUser(userDTO)) {
            return dao.registerUser(userDTO);
        } else {
            throw new MissingFieldException("Please fill all requested fields");
        }
    }

    @PostMapping(value = "/user/login")
    public User logIn(@RequestBody UserDTO userDTO) throws WrongCredentialsException, MissingFieldException {
        if (validateDTO(userDTO)) {
            return dao.logInUser(userDTO);
        } else {
            throw new MissingFieldException("Please fill all necessary fields");
        }
    }

    @GetMapping(value = "/userss")
    public List<User> getAllUsers() {
        return dao.getAllUsers();
    }

    private boolean validateDTO(UserDTO userDTO) {
        return !userDTO.getUsername().isEmpty() &&
               !userDTO.getPassword().isEmpty();
    }

    private boolean validateUser(UserDTO user) {
        return  !user.getUsername().isEmpty() &&
                !user.getPassword().isEmpty() &&
                !user.getEmail().isEmpty() &&
                !user.getFirstName().isEmpty() &&
                !user.getLastName().isEmpty() &&
                user.getAge() >= 0;
    }

    @ExceptionHandler(MissingFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMissingFieldException(MissingFieldException e) {
        return e.getMessage();
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return e.getMessage();
    }

    @ExceptionHandler(WrongCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleWrongCredentialsException(WrongCredentialsException e) {
        return e.getMessage();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUserNotFoundException(UserNotFoundException e) {
        return e.getMessage();
    }
}



