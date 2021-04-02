package com.finals.kinoarena.util;

import com.finals.kinoarena.util.exceptions.UnauthorizedException;
import com.finals.kinoarena.model.entity.User;
import com.finals.kinoarena.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class SessionManager {

    public static final String LOGGED_USER = "LoggedUser";

    @Autowired
    UserRepository repository;


    public void loginUser(HttpSession ses, int id) {
        ses.setAttribute(LOGGED_USER, id);
    }

    public boolean isLogged(HttpSession ses) {
        return ses.getAttribute(LOGGED_USER) != null;
    }

    public User getLoggedUser(HttpSession ses) throws UnauthorizedException {
        if (!isLogged(ses)) {
            throw new UnauthorizedException("You need to be logged in");
        }
        int userId = (int) ses.getAttribute(LOGGED_USER);
        return repository.findById(userId).get();
    }

    public void logoutUser(HttpSession ses) {
        ses.invalidate();
    }
}


