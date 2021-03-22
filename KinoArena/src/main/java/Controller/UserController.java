package Controller;

import DAO.UserDao;
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

    @PostMapping(value = "user/register")
    public void registerUser(@RequestBody User user) throws userAlreadyExistsException {
        if (userDao.getByEmail(user) != null){
            if (userDao.getByUserName(user) != null){
                userDao.registerUser(user);
            }else throw new userAlreadyExistsException("A user with that username already exists");
        } else{
            throw new userAlreadyExistsException("A user with that Email already exists");
        }
    }

    @PostMapping(value = "user/login")
    public void logIn(@RequestBody User user){

    }
}
