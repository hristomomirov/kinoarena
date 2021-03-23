package com.finals.kinoarena.DAO;

import com.finals.kinoarena.DTO.UserDTO;
import com.finals.kinoarena.Handler.UserAlreadyExistsException;
import com.finals.kinoarena.Handler.UserNotFoundException;
import com.finals.kinoarena.Handler.WrongCredentialsException;
import com.finals.kinoarena.Model.User;
import com.finals.kinoarena.Model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
public class UserDao extends AbstractDao {

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

    public User registerUser(UserDTO userDTO) throws UserAlreadyExistsException {
        if (emailExist(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("There is already a user with that email address: " + userDTO.getEmail());
        }
        if (usernameExists(userDTO.getUsername())) {
            throw new UserAlreadyExistsException("There is already a user with that username: " + userDTO.getUsername());
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        //TODO needs rework
        int age = userDTO.getAge();
        if (age <= 19) {
            user.setStatusId(1);
        } else if (age <= 65) {
            user.setStatusId(2);
        } else {
            user.setStatusId(3);
        }
        user.setRoleId(1);
        user.setCreatedAt(LocalDateTime.now());
        return repository.save(user);
    }

    private boolean usernameExists(String username) {
        return getByUsername(username) != null;
    }

    private boolean emailExist(String email) {
        return getByEmail(email) != null;
    }


    public User getById(long id) throws UserNotFoundException {

        if (repository.findById(id).isPresent()) {
            return repository.findById(id).get();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User logInUser(UserDTO userDTO) throws WrongCredentialsException {
        //TODO verify crypted password
        if (verifyUsername(userDTO.getUsername()) && verifyPassword(userDTO)) {
            return getByUsername(userDTO.getUsername());
        } else {
            throw new WrongCredentialsException("Username or Password incorrect");
        }
    }

    private boolean verifyPassword(UserDTO userDTO) {
        String password = repository.findByUsername(userDTO.getUsername()).getPassword();
        return userDTO.getPassword().equals(password);
    }

    private boolean verifyUsername(String username) {
        return repository.findByUsername(username) != null;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}
