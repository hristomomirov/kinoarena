package com.finals.kinoarena.Controller;

import com.finals.kinoarena.Model.DTO.RequestLoginUserDTO;
import com.finals.kinoarena.Model.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class SessionManager {

    public static final String LOGGED_USER = "LoggedUser";

    @Autowired
    UserRepository repository;

    public  boolean isLogged(HttpSession ses) {
        return ses.getAttribute(LOGGED_USER) != null;
    }

    public void loginUser(HttpSession ses, RequestLoginUserDTO dto) {
        int id = repository.findByUsername(dto.getUsername()).getId();
        ses.setAttribute(LOGGED_USER, id);

    }

}

