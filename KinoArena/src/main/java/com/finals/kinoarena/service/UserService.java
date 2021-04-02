package com.finals.kinoarena.service;

import com.finals.kinoarena.util.exceptions.BadRequestException;
import com.finals.kinoarena.model.DTO.RegisterDTO;
import com.finals.kinoarena.model.DTO.EditUserPasswordDTO;
import com.finals.kinoarena.model.DTO.UserWithoutPassDTO;
import com.finals.kinoarena.model.DTO.UserWithoutTicketAndPassDTO;
import com.finals.kinoarena.model.entity.ConfirmationToken;
import com.finals.kinoarena.model.entity.User;
import com.finals.kinoarena.model.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService extends AbstractService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

// TODO Transaction
    public UserWithoutPassDTO registerUser(RegisterDTO registerDTO) throws BadRequestException {
        if (emailExist(registerDTO.getEmail())) {
            throw new BadRequestException("There is already a user with that email address: " + registerDTO.getEmail());
        }
        if (usernameExists(registerDTO.getUsername())) {
            throw new BadRequestException("There is already a user with that username: " + registerDTO.getUsername());
        }
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new BadRequestException("Passwords must match");
        }
        registerDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        User user = new User(registerDTO);
        user = userRepository.save(user);
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);
        return new UserWithoutPassDTO(user);
    }

    public UserWithoutTicketAndPassDTO logInUser(String username, String password) throws BadRequestException {
        if (verifyUsername(username) && verifyPassword(username, password)) {
            User user = getByUsername(username);
            return new UserWithoutTicketAndPassDTO(user);
        } else {
            throw new BadRequestException("Username or Password incorrect");
        }
    }

    public UserWithoutPassDTO changePassword(EditUserPasswordDTO passwordDTO) throws BadRequestException {
        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            throw new BadRequestException("Passwords must match");
        }
        User user = userRepository.findById(passwordDTO.getId()).get();
        if (verifyPassword(user.getUsername(), passwordDTO.getOldPassword())) {
            user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        }
        return new UserWithoutPassDTO(userRepository.save(user));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private boolean usernameExists(String username) {
        return getByUsername(username) != null;
    }

    private boolean emailExist(String email) {
        return getByEmail(email) != null;
    }

    private boolean verifyPassword(String username, String password) {
        String hashedPass = userRepository.findByUsername(username).getPassword();
        return passwordEncoder.matches(password, hashedPass);

    }

    private boolean verifyUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
