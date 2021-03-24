package com.finals.kinoarena.Controller;


import com.finals.kinoarena.Exceptions.*;
import com.finals.kinoarena.Interfaces.IRegistrationLogin;
import com.finals.kinoarena.Service.CinemaService;
import com.finals.kinoarena.Model.DTO.CinemaDTO;
import com.finals.kinoarena.Model.Entity.Cinema;
import com.finals.kinoarena.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


@Component
@RestController
public class CinemaController extends AbstractController implements IRegistrationLogin {

    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private UserService userService;


    @GetMapping(value = "/cinemas")
    public List<CinemaDTO> getAllCinemas() throws MissingCinemasInDBException {
      return   cinemaService.getAllCinemas();
    }

    @GetMapping(value = "/cinema/id/{id}")
    public CinemaDTO getCinemaById(@PathVariable int id){
        return cinemaService.getCinemaByID(id);
    }

    @GetMapping(value = "/cinemas/city/{city}")
    public List<CinemaDTO> getAllCinemasByCity(@PathVariable String city) throws MissingCinemasInDBException {
        return cinemaService.getAllCinemasByCity(city);
    }

    @PutMapping(value = "/cinemas")
    public Cinema addCinema(@RequestBody CinemaDTO cinemaDTO, HttpSession ses) throws AlreadyLoggedException, UserNotFoundException, NotAdminException, CinemaAlreadyExistException {
        if(!isLogged(ses)) {
            throw new UserNotFoundException("You need to be logged to have that functionality");
        }
        int userId = (int) ses.getAttribute("LoggedUser");
        if(userService.getById(userId).getRoleId()!=2){
            throw new NotAdminException("Only admins can add new cinemas");
        }
        return  cinemaService.addCinema(cinemaDTO);
    }



}
