package com.finals.kinoarena.Service;

import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Model.DTO.RequestRegisterUserDTO;

import com.finals.kinoarena.Model.DTO.UserPasswordDTO;
import com.finals.kinoarena.Model.DTO.UserWithoutPassDTO;
import com.finals.kinoarena.Model.Entity.User;
import com.finals.kinoarena.Model.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Optional;


@Component
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User getByEmail(String email) {
        return repository.findByEmail(email);
    }

    public UserWithoutPassDTO registerUser(RequestRegisterUserDTO requestRegisterUserDTO) throws BadRequestException {
        if (emailExist(requestRegisterUserDTO.getEmail())) {
            throw new BadRequestException("There is already a user with that email address: " + requestRegisterUserDTO.getEmail());
        }
        if (usernameExists(requestRegisterUserDTO.getUsername())) {
            throw new BadRequestException("There is already a user with that username: " + requestRegisterUserDTO.getUsername());
        }
        if (!requestRegisterUserDTO.getPassword().equals(requestRegisterUserDTO.getConfirmPassword())) {
            throw new BadRequestException("Passwords must match");
        }
        requestRegisterUserDTO.setPassword(passwordEncoder.encode(requestRegisterUserDTO.getPassword()));
        User user = new User(requestRegisterUserDTO);
        return new UserWithoutPassDTO(repository.save(user));
    }

    private boolean usernameExists(String username) {
        return getByUsername(username) != null;
    }

    private boolean emailExist(String email) {
        return getByEmail(email) != null;
    }


    public User getById(int id) throws BadRequestException {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new BadRequestException("User does not exist");
        }
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public UserWithoutPassDTO logInUser(String username, String password) throws BadRequestException {
        if (verifyUsername(username) && verifyPassword(username, password)) {
            User user = getByUsername(username);
            return new UserWithoutPassDTO(user);
        } else {
            throw new BadRequestException("Username or Password incorrect");
        }
    }

    private boolean verifyPassword(String username, String password) {
        String hashedPass = repository.findByUsername(username).getPassword();
        return passwordEncoder.matches(password, hashedPass);

    }

    private boolean verifyUsername(String username) {
        return repository.findByUsername(username) != null;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public UserWithoutPassDTO changePassword(UserPasswordDTO passwordDTO) throws BadRequestException {
        Optional<User> sUser = repository.findById(passwordDTO.getId());
        if (sUser.isPresent()) {
            User user = sUser.get();
            user.setPassword(passwordEncoder.encode(passwordDTO.getPassword()));
            return new UserWithoutPassDTO(repository.save(user));
        } else {
            throw new BadRequestException("User does not exist");
        }
    }
}
