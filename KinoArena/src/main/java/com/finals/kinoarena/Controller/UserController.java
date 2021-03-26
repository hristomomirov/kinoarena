package com.finals.kinoarena.Controller;

import com.finals.kinoarena.Model.DTO.*;
import com.finals.kinoarena.Service.UserService;
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

    @Autowired
    private UserService service;
    @Autowired
    private SessionManager sessionManager;

    @PutMapping(value = "/users")
    public UserWithoutPassDTO registerNewUser(@RequestBody RegisterDTO registerDTO, HttpSession ses) throws BadRequestException, UnauthorizedException {
        if (!sessionManager.isLogged(ses)) {
            if (validateRegister(registerDTO)) {
                return service.registerUser(registerDTO);
            }
            throw new BadRequestException("Please fill all requested fields");
        }
        throw new UnauthorizedException("You are currently signed in to an account.Please logout");
    }

    @PostMapping(value = "/users")
    public UserWithoutTicketAndPassDTO login(@RequestBody LoginDTO loginDTO, HttpSession ses) throws BadRequestException, UnauthorizedException {
        if (!sessionManager.isLogged(ses)) {
            if (validateLogIn(loginDTO)) {
                UserWithoutTicketAndPassDTO dto = service.logInUser(loginDTO.getUsername(), loginDTO.getPassword());
                sessionManager.loginUser(ses, dto.getId());
                return dto;
            }
            throw new BadRequestException("Please fill all necessary fields");
        }
        throw new UnauthorizedException("You are currently signed in to an account.Please logout");
    }

    @GetMapping(value = "/users")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @PostMapping(value = "/edit")
    public UserWithoutPassDTO changePassword(@RequestBody UserPasswordDTO passwordDTO, HttpSession ses) throws UnauthorizedException, BadRequestException {
        User user = sessionManager.getLoggedUser(ses);
        passwordDTO.setId(user.getId());
        if (validatePassword(passwordDTO.getNewPassword()) && validatePassword(passwordDTO.getConfirmPassword()) && validatePassword(passwordDTO.getOldPassword())) {
            return service.changePassword(passwordDTO);
        }
        throw new BadRequestException("Password must be between 8 and 20 symbols and must contain at least one upper and lower case letter and number");
    }

    private boolean validateLogIn(LoginDTO loginDTO) {
        return !loginDTO.getUsername().isBlank() &&
                !loginDTO.getPassword().isBlank();
    }

    @PostMapping(value = "/logout")
    public String logout(HttpSession ses) throws UnauthorizedException {
        if (!sessionManager.isLogged(ses)) {
            throw new UnauthorizedException("You need to be logged in");
        }
        sessionManager.logoutUser(ses);
        return "You have been successfully logged out";
    }

    private boolean validateRegister(RegisterDTO registerDTO) throws BadRequestException {
        return validateUsername(registerDTO.getUsername()) &&
                validatePassword(registerDTO.getPassword()) &&
                validatePassword(registerDTO.getConfirmPassword()) &&
                validateEmail(registerDTO.getEmail()) &&
                validateName(registerDTO.getFirstName(), registerDTO.getLastName()) &&
                validateStatus(registerDTO.getStatus()) &&
                validateAge(registerDTO.getAge());
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

    private boolean validatePassword(String password) throws BadRequestException {
        if (password.isBlank()) {
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






