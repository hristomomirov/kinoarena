package com.finals.kinoarena.DAO;

import com.finals.kinoarena.DTO.UserDTO;
import com.finals.kinoarena.Handler.UserAlreadyExistsException;
import com.finals.kinoarena.Handler.UserNotFoundException;
import com.finals.kinoarena.Handler.WrongCredentialsException;
import com.finals.kinoarena.Model.User;
import com.finals.kinoarena.Model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
public class UserDao extends AbstractDao {

    @Autowired
    private UserRepository repository;

    public User getByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User getByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User registerUser(User user) throws UserAlreadyExistsException {
        if (getByEmail(user.getEmail()) == null) {
            if (getByUsername(user.getUsername()) == null) {
                //TODO needs rework
                int age = user.getAge();
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
            } else {
                throw new UserAlreadyExistsException("A user with that username already exists");
            }
        } else {
            throw new UserAlreadyExistsException("A user with that email already exists");
        }
    }


    public User getById(long id) throws UserNotFoundException {

        if (repository.findById(id).isPresent()){
            return repository.findById(id).get();
        }else {
            throw new UserNotFoundException("User not found");
        }
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User logInUser(UserDTO userDTO) throws WrongCredentialsException {
        if (verifyUsername(userDTO.getUsername()) && verifyPassword(userDTO)){
            return getByUsername(userDTO.getUsername());
        }else {
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
}
