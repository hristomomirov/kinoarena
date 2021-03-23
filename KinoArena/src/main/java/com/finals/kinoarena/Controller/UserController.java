package com.finals.kinoarena.Controller;

import com.finals.kinoarena.DAO.UserDao;
import com.finals.kinoarena.Handler.MissingFieldException;
import com.finals.kinoarena.Handler.WrongCredentialsException;
import com.finals.kinoarena.Handler.UserAlreadyExistsException;
import com.finals.kinoarena.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Component
@RestController
public class UserController {

    @Autowired
    private UserDao dao;


    @PostMapping(value = "/user/register")
    public User registerUser(@RequestBody User user) throws UserAlreadyExistsException, MissingFieldException {
        if (validateUser(user)){
            return dao.registerUser(user);
        }else{
            throw new MissingFieldException("Please fill all necessary fields");
        }
    }

    private boolean validateUser(User user) {
        return false; //TODO
    }

    @PostMapping(value = "/user/login")
    public User logIn(@RequestBody User user) throws WrongCredentialsException {
        User user1 = dao.getByUsername(user.getUsername());
        //TODO
        if (user1 != null) {
            if (user.getPassword().equals(user1.getPassword())) {
                return dao.getById(user1.getId());
            } else {
                throw new WrongCredentialsException("Username or Password incorrect");
            }
        } else {
            throw new WrongCredentialsException("Username or Password incorrect");
        }
    }
    @GetMapping(value = "/userss")
    public List<User> getAllUsers(){
return dao.getAllUsers();
    }
//
//    private boolean validateUser(User user) {
//        if (!user.getUsername().isEmpty() &&
//            !user.getPassword().isEmpty() &&
//            !user.get)
//    }
}
