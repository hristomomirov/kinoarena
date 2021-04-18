package com.finals.kinoarena.controller;

import com.fasterxml.jackson.annotation.JsonValue;
import com.finals.kinoarena.util.exceptions.BadRequestException;
import com.finals.kinoarena.util.exceptions.UnauthorizedException;
import com.finals.kinoarena.model.DTO.*;
import com.finals.kinoarena.model.entity.ConfirmationToken;
import com.finals.kinoarena.model.repository.ConfirmationTokenRepository;
import com.finals.kinoarena.model.repository.UserRepository;
import com.finals.kinoarena.service.EmailSenderService;
import com.finals.kinoarena.service.UserService;
import com.finals.kinoarena.model.entity.User;
import com.finals.kinoarena.model.entity.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Component
@RestController
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private UserRepository userRepository;


    @PostMapping(value = "/users")
    public String registerUser(@Valid @RequestBody RegisterDTO registerDTO, HttpSession ses) throws BadRequestException, UnauthorizedException {
        if (sessionManager.isLogged(ses)) {
            throw new UnauthorizedException("You are currently signed in to an account.Please logout");
        }
        if (!validateStatus(registerDTO.getStatus())){
            throw new BadRequestException("Incorrect status");
        }
        UserWithoutPassDTO register = userService.registerUser(registerDTO);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(registerDTO.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("chand312902@gmail.com");
        mailMessage.setText("To confirm your account, please click here : " +
                            "http://localhost:8888/confirm-account?token=" +
                            confirmationTokenRepository.findByUserId(register.getId()).getConfirmationToken());
                            emailSenderService.sendEmail(mailMessage);
        return "A confirmation email was sent to " + registerDTO.getEmail();
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(@RequestParam("token") String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if (token == null) {
            return "Error!Message in the link is broken or missing";
        }
        User user = userRepository.findByEmail(token.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
        return "Account verified";
    }

    @PostMapping(value = "/users/login")
    public UserWithoutTicketAndPassDTO login(@Valid @RequestBody LoginDTO loginDTO, HttpSession ses) throws BadRequestException, UnauthorizedException {
        if (sessionManager.isLogged(ses)) {
            throw new UnauthorizedException("You are currently signed in to an account.Please logout");
        }
        if (!userRepository.findByUsername(loginDTO.getUsername()).isEnabled()) {
            throw new BadRequestException("You need to verify your email first");
        }
        UserWithoutTicketAndPassDTO user = userService.logInUser(loginDTO.getUsername(), loginDTO.getPassword());
        sessionManager.loginUser(ses, user.getId());
        return user;
    }

    @PutMapping(value = "/users/edit")
    public UserWithoutTicketAndPassDTO changePassword(@RequestBody EditUserPasswordDTO passwordDTO, HttpSession ses) throws UnauthorizedException, BadRequestException {
        User user = sessionManager.getLoggedUser(ses);
        int userId = user.getId();
        return userService.changePassword(passwordDTO,userId);
    }

    @PostMapping(value = "/users/logout")
    public String logout(HttpSession ses) throws UnauthorizedException {
        if (!sessionManager.isLogged(ses)) {
            throw new UnauthorizedException("You need to be logged in");
        }
        sessionManager.logoutUser(ses);
        return "You have been successfully logged out";
    }

    private boolean validateStatus(String status) {
        for (UserStatus s : UserStatus.values()) {
            if (s.toString().equals(status.toUpperCase())) {
                return true;
            }
        }
       return false;
    }
}
