package com.finals.kinoarena.Interfaces;

import javax.servlet.http.HttpSession;

public interface IRegistrationLogin {

    default public boolean isLogged(HttpSession session) {
        return !(session.getAttribute("LoggedUser") == null);
    }
}
