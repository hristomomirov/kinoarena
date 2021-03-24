package com.finals.kinoarena.Controller;

import com.finals.kinoarena.Model.DAO.UserDao;
import com.finals.kinoarena.Model.DTO.UserDTO;
import com.finals.kinoarena.Handler.*;
import com.finals.kinoarena.Model.Entity.User;
import com.finals.kinoarena.Model.Entity.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RestController
public class UserController {

    @Autowired
    private UserDao dao;

    @PutMapping(value = "/users")
    public User registerNewUser(@RequestBody UserDTO userDTO) throws UserAlreadyExistsException, MissingFieldException, BadCredentialsException {
        if (validateUser(userDTO)) {
            return dao.registerUser(userDTO);
        } else {
            throw new MissingFieldException("Please fill all requested fields");
        }
    }

    @PostMapping(value = "/users")
    public User logIn(@RequestBody UserDTO userDTO) throws WrongCredentialsException, MissingFieldException {
        if (validateUserDTO(userDTO)) {
            return dao.logInUser(userDTO);
        } else {
            throw new MissingFieldException("Please fill all necessary fields");
        }
    }

    @GetMapping(value = "/users")
    public List<User> getAllUsers() {
        return dao.getAllUsers();
    }

    @PostMapping(value = "/user/edit")
    public User editProfile(@RequestBody UserDTO userDTO) {
        return null;
    }

    private boolean validateUserDTO(UserDTO userDTO) {
        return !userDTO.getUsername().isEmpty() &&
                !userDTO.getPassword().isEmpty();
    }

    private boolean validateUser(UserDTO userDTO) throws BadCredentialsException, MissingFieldException {
        return validateUsername(userDTO.getUsername()) &&
                validatePassword(userDTO.getPassword()) &&
                validateEmail(userDTO.getEmail()) &&
                validateName(userDTO.getFirstName(), userDTO.getLastName()) &&
                validateStatus(userDTO.getStatus()) &&
                validateAge(userDTO.getAge());
    }

    private boolean validateAge(Integer age) throws BadCredentialsException, MissingFieldException {
        //TODO age cant be letters
        if (age == null) {
            throw new MissingFieldException("Please fill all necessary fields");
        }
        if (age >= 0 && age <= 120) {
            return true;
        }
        throw new BadCredentialsException("Incorrect age.Age cannot be less than 0 and more than 120");
    }

    private boolean validateStatus(String status) throws BadCredentialsException, MissingFieldException {
        if (status.isBlank()) {
            throw new MissingFieldException("Please fill all necessary fields");
        }
        for (UserStatus s : UserStatus.values()) {
            if (s.toString().equals(status.toUpperCase())) {
                return true;
            }
        }
        throw new BadCredentialsException("Incorrect status");
    }

    private boolean validateName(String firstName, String lastName) throws MissingFieldException, BadCredentialsException {
        if (firstName.isBlank() || lastName.isBlank()) {
            throw new MissingFieldException("Please fill all necessary fields");
        }
        String regex = "[a-zA-Z]+";
        if (firstName.matches(regex) && lastName.matches(regex)) {
            return true;
        }
        throw new BadCredentialsException("Name must contain only letters");
    }

    private boolean validateEmail(String email) throws MissingFieldException {
        if (email.isBlank()) {
            throw new MissingFieldException("Please fill all necessary fields");
        }
        return true;
    }

    private boolean validatePassword(String password) throws MissingFieldException, BadCredentialsException {
        if (password.isBlank()) {
            throw new MissingFieldException("Please fill all necessary fields");
        }
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        if (m.matches()) {
            return true;
        }
        throw new BadCredentialsException("Password must be between 8 and 20 symbols and must contain at least one upper and lower case letter and number");
    }

    private boolean validateUsername(String username) throws BadCredentialsException, MissingFieldException {
        if (username.isBlank()) {
            throw new MissingFieldException("Please fill all necessary fields");
        }
        String regex = "^[a-zA-Z0-9]*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(username);
        if (m.matches()) {
            return true;
        }
        throw new BadCredentialsException("Username must include only letters and numbers");
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

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUserBadCredentialsException(BadCredentialsException e) {
        return e.getMessage();
    }
}



