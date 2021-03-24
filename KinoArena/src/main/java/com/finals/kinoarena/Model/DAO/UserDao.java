package com.finals.kinoarena.Model.DAO;

import com.finals.kinoarena.Model.DTO.UserDTO;
import com.finals.kinoarena.Exceptions.UserAlreadyExistsException;
import com.finals.kinoarena.Exceptions.UserNotFoundException;
import com.finals.kinoarena.Exceptions.WrongCredentialsException;
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
        user.setStatusId(UserStatus.valueOf(userDTO.getStatus().toUpperCase()).ordinal() + 1);
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


    public User getById(int id) throws UserNotFoundException {
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
        if (verifyUsername(userDTO.getUsername()) && verifyPassword(userDTO)) {
            return getByUsername(userDTO.getUsername());
        } else {
            throw new WrongCredentialsException("Username or Password incorrect");
        }
    }

    private boolean verifyPassword(UserDTO userDTO) {
        String password = repository.findByUsername(userDTO.getUsername()).getPassword();
        return passwordEncoder.matches(userDTO.getPassword(), password);

    }

    private boolean verifyUsername(String username) {
        return repository.findByUsername(username) != null;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}
