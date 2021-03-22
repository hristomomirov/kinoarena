package Controller;

import DAO.UserDao;
import Handler.WrongCredentialsException;
import Handler.userAlreadyExistsException;
import Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @PostMapping(value = "/user/register")
    public void registerUser(@RequestBody User user) throws userAlreadyExistsException {
        if (userDao.getByEmail(user.getUsername()) != null) {
            //TODO
            if (userDao.getbyusername(user.getEmail()) != null) {
                userDao.registerUser(user);
            } else throw new userAlreadyExistsException("A user with that username already exists");
        } else {
            throw new userAlreadyExistsException("A user with that Email already exists");
        }
    }

    @PostMapping(value = "/user/login")
    public User logIn(@RequestBody User user) throws WrongCredentialsException {
        User user1 = userDao.getbyusername(user.getUsername());
        //TODO
        if (user1 != null) {
            if (user.getPassword().equals(user1.getPassword())) {
                return userDao.getById(user1.getId());
            } else {
                throw new WrongCredentialsException("Username or Password incorrect");
            }
        } else {
            throw new WrongCredentialsException("Username or Password incorrect");
        }
    }
}
