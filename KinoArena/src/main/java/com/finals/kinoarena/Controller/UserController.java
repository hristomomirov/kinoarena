package com.finals.kinoarena.Controller;

import com.finals.kinoarena.Model.DTO.UserPasswordDTO;
import com.finals.kinoarena.Service.UserService;
import com.finals.kinoarena.Model.DTO.RequestLoginUserDTO;
import com.finals.kinoarena.Model.DTO.UserWithoutPassDTO;
import com.finals.kinoarena.Model.DTO.RequestRegisterUserDTO;
import com.finals.kinoarena.Exceptions.*;
import com.finals.kinoarena.Model.Entity.User;
import com.finals.kinoarena.Model.Entity.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RestController
public class UserController extends AbstractController {

    public static final String LOGGED_USER = "LoggedUser";
    @Autowired
    private UserService service;
    @Autowired
    private SessionManager sessionManager;

    @PutMapping(value = "/users")
    public UserWithoutPassDTO registerNewUser(@RequestBody RequestRegisterUserDTO requestRegisterUserDTO, HttpSession ses) throws BadRequestException, UnauthorizedException {
        if (sessionManager.isLogged(ses)) {
            throw new UnauthorizedException("You are currently signed in to an account.Please logout");
        }
        if (validateRegister(requestRegisterUserDTO)) {
            return service.registerUser(requestRegisterUserDTO);
        } else {
            throw new BadRequestException("Please fill all requested fields");
        }
    }

    @PostMapping(value = "/users")
    public UserWithoutPassDTO login(@RequestBody RequestLoginUserDTO requestLoginUserDTO, HttpSession ses) throws BadRequestException {
        if (validateLogIn(requestLoginUserDTO)) {
            UserWithoutPassDTO r = service.logInUser(requestLoginUserDTO.getUsername(), requestLoginUserDTO.getPassword());
            ses.setAttribute(LOGGED_USER, r.getId());  //pavel : трябваше ми за тестване
            return r;
        } else {
            throw new BadRequestException("Please fill all necessary fields");
        }
    }

    @GetMapping(value = "/users")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @PostMapping(value = "/{id}/edit")
    public UserWithoutPassDTO changePassword(@RequestBody UserPasswordDTO passwordDTO, HttpSession ses, @PathVariable int id) throws UnauthorizedException, BadRequestException {
        if (ses.getAttribute(LOGGED_USER) == null) {
            throw new UnauthorizedException("You need to be logged in");
        }
        int loggedId = (int) ses.getAttribute(LOGGED_USER);
        if (loggedId != id) {
            throw new BadRequestException("You can only edit your OWN account");

        }
        if (validatePassword(passwordDTO.getPassword(), passwordDTO.getConfirmPassword())) {
            passwordDTO.setId(id);
           return service.changePassword(passwordDTO);
        }
        throw new BadRequestException("Password must be between 8 and 20 symbols and must contain at least one upper and lower case letter and number");
    }

    private boolean validateLogIn(RequestLoginUserDTO requestLoginUserDTO) {
        return !requestLoginUserDTO.getUsername().isBlank() &&
                !requestLoginUserDTO.getPassword().isBlank();
    }

    @PostMapping(value = "/logout")
//    public String logout(HttpSession ses) throws BadRequestException {
//        if (validateLogIn(requestLoginUserDTO)) {
//            UserWithoutPassDTO r = service.logInUser(requestLoginUserDTO.getUsername(), requestLoginUserDTO.getPassword());
//            ses.setAttribute(LOGGED_USER, r.getId());  //pavel : трябваше ми за тестване
//            return r;
//        } else {
//            throw new BadRequestException("Please fill all necessary fields");
//        }
//    }

    private boolean validateRegister(RequestRegisterUserDTO requestRegisterUserDTO) throws BadRequestException {
        return validateUsername(requestRegisterUserDTO.getUsername()) &&
                validatePassword(requestRegisterUserDTO.getPassword(), requestRegisterUserDTO.getConfirmPassword()) &&
                validateEmail(requestRegisterUserDTO.getEmail()) &&
                validateName(requestRegisterUserDTO.getFirstName(), requestRegisterUserDTO.getLastName()) &&
                validateStatus(requestRegisterUserDTO.getStatus()) &&
                validateAge(requestRegisterUserDTO.getAge());
    }

    private boolean validateAge(Integer age) throws BadRequestException {
        //TODO age cant be letters
        if (age == null) {
            throw new BadRequestException("Please fill all necessary fields");
        }
        if (age >= 0 && age <= 120) {
            return true;
        }
        throw new BadRequestException("Incorrect age.Age cannot be less than 0 and more than 120");
    }

    private boolean validateStatus(String status) throws BadRequestException {
        if (status.isBlank()) {
            throw new BadRequestException("Please fill all necessary fields");
        }
        for (UserStatus s : UserStatus.values()) {
            if (s.toString().equals(status.toUpperCase())) {
                return true;
            }
        }
        throw new BadRequestException("Incorrect status");
    }

    private boolean validateName(String firstName, String lastName) throws BadRequestException {
        if (firstName.isBlank() || lastName.isBlank()) {
            throw new BadRequestException("Please fill all necessary fields");
        }
        String regex = "[a-zA-Z]+";
        if (firstName.matches(regex) && lastName.matches(regex)) {
            return true;
        }
        throw new BadRequestException("Name must contain only letters");
    }

    private boolean validateEmail(String email) throws BadRequestException {
        if (email.isBlank()) {
            throw new BadRequestException("Please fill all necessary fields");
        }
        return true;
    }

    //TODO
    private boolean validatePassword(String password, String confirmPassword) throws BadRequestException {
        if (password.isBlank() || confirmPassword.isBlank()) {
            throw new BadRequestException("Please fill all necessary fields");
        }
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        if (m.matches()) {
            return true;
        }
        throw new BadRequestException("Password must be between 8 and 20 symbols and must contain at least one upper and lower case letter and number");
    }

    private boolean validateUsername(String username) throws BadRequestException {
        if (username.isBlank()) {
            throw new BadRequestException("Please fill all necessary fields");
        }
        String regex = "^[a-zA-Z0-9]*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(username);
        if (m.matches()) {
            return true;
        }
        throw new BadRequestException("Username must include only letters and numbers");
    }
}



