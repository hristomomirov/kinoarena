package com.finals.kinoarena.Srvice;

import com.finals.kinoarena.Exceptions.BadCredentialsException;
import com.finals.kinoarena.Model.DTO.RequestLoginUserDTO;
import com.finals.kinoarena.Model.DTO.RequestRegisterUserDTO;
import com.finals.kinoarena.Exceptions.UserAlreadyExistsException;
import com.finals.kinoarena.Exceptions.UserNotFoundException;
import com.finals.kinoarena.Exceptions.WrongCredentialsException;
import com.finals.kinoarena.Model.DTO.ResponseLoginUserDTO;
import com.finals.kinoarena.Model.DTO.ResponseRegisterUserDTO;
import com.finals.kinoarena.Model.Entity.User;
import com.finals.kinoarena.Model.Repository.UserRepository;
import com.finals.kinoarena.Model.Entity.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

    public ResponseRegisterUserDTO registerUser(RequestRegisterUserDTO requestRegisterUserDTO) throws UserAlreadyExistsException, BadCredentialsException {
        if (emailExist(requestRegisterUserDTO.getEmail())) {
            throw new UserAlreadyExistsException("There is already a user with that email address: " + requestRegisterUserDTO.getEmail());
        }
        if (usernameExists(requestRegisterUserDTO.getUsername())) {
            throw new UserAlreadyExistsException("There is already a user with that username: " + requestRegisterUserDTO.getUsername());
        }
        if (!requestRegisterUserDTO.getPassword().equals(requestRegisterUserDTO.getConfirmPassword())){
            throw new BadCredentialsException("Passwords must match");
        }
        requestRegisterUserDTO.setPassword(passwordEncoder.encode(requestRegisterUserDTO.getPassword()));
        User user = new User(requestRegisterUserDTO);
        return new ResponseRegisterUserDTO(repository.save(user));
    }

    private boolean usernameExists(String username) {
        return getByUsername(username) != null;
    }

    private boolean emailExist(String email) {
        return getByEmail(email) != null;
    }


    public User getById(int id) throws UserNotFoundException {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public ResponseLoginUserDTO logInUser(String username, String password) throws WrongCredentialsException {
        if (verifyUsername(username) && verifyPassword(username,password)) {
            User user = getByUsername(username);
            return new ResponseLoginUserDTO(user);
        } else {
            throw new WrongCredentialsException("Username or Password incorrect");
        }
    }

    private boolean verifyPassword(String username,String password) {
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

}
