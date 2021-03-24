package com.finals.kinoarena.Controller;

import com.finals.kinoarena.Model.DTO.RequestLoginUserDTO;
import com.finals.kinoarena.Model.DTO.ResponseLoginUserDTO;
import com.finals.kinoarena.Model.DTO.ResponseRegisterUserDTO;
import com.finals.kinoarena.Srvice.UserService;
import com.finals.kinoarena.Model.DTO.RequestRegisterUserDTO;
import com.finals.kinoarena.Exceptions.*;
import com.finals.kinoarena.Model.Entity.User;
import com.finals.kinoarena.Model.Entity.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RestController
public class UserController extends AbstractController {

    @Autowired
    private UserService service;

    @PutMapping(value = "/users")
    public ResponseRegisterUserDTO registerNewUser(@RequestBody RequestRegisterUserDTO requestRegisterUserDTO) throws UserAlreadyExistsException, MissingFieldException, BadCredentialsException {
        if (validateRegister(requestRegisterUserDTO)) {
            return service.registerUser(requestRegisterUserDTO);
        } else {
            throw new MissingFieldException("Please fill all requested fields");
        }
    }
//TODO Interceptor,session
    @PostMapping(value = "/users")
    public ResponseLoginUserDTO login(@RequestBody RequestLoginUserDTO requestLoginUserDTO) throws WrongCredentialsException, MissingFieldException {
        if (validateLogIn(requestLoginUserDTO)) {
            return service.logInUser(requestLoginUserDTO.getUsername(),requestLoginUserDTO.getPassword());
        } else {
            throw new MissingFieldException("Please fill all necessary fields");
        }
    }

    @GetMapping(value = "/users")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @PostMapping(value = "/user/edit")
    public User editProfile(@RequestBody RequestRegisterUserDTO requestRegisterUserDTO) {
        return null;
    }

    private boolean validateLogIn(RequestLoginUserDTO requestLoginUserDTO) {
        return !requestLoginUserDTO.getUsername().isEmpty() &&
                !requestLoginUserDTO.getPassword().isEmpty();
    }

    private boolean validateRegister(RequestRegisterUserDTO requestRegisterUserDTO) throws BadCredentialsException, MissingFieldException {
        return validateUsername(requestRegisterUserDTO.getUsername()) &&
                validatePassword(requestRegisterUserDTO.getPassword(),requestRegisterUserDTO.getConfirmPassword()) &&
                validateEmail(requestRegisterUserDTO.getEmail()) &&
                validateName(requestRegisterUserDTO.getFirstName(), requestRegisterUserDTO.getLastName()) &&
                validateStatus(requestRegisterUserDTO.getStatus()) &&
                validateAge(requestRegisterUserDTO.getAge());
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

    private boolean validatePassword(String password, String confirmPassword) throws MissingFieldException, BadCredentialsException {
        if (password.isBlank() || confirmPassword.isBlank()) {
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
}



