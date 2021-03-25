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
    public CinemaDTO addCinema(@RequestBody CinemaDTO cinemaDTO, HttpSession ses) throws AlreadyLoggedException, UserNotFoundException, NotAdminException, CinemaAlreadyExistException, MissingFieldException, BadCredentialsException {
        if(!isLogged(ses)) {
            throw new UserNotFoundException("You need to be logged to have that functionality");
        }
        int userId = (int) ses.getAttribute("LoggedUser");
        if(userService.getById(userId).getRoleId()!=2){
            throw new NotAdminException("Only admins can add new cinemas");
        }
        if(!validateCinemaFields(cinemaDTO.getCity(),cinemaDTO.getName())){
            throw new MissingFieldException("Please fill all requested fields");
        }
            return  cinemaService.addCinema(cinemaDTO);

    }

    @DeleteMapping(value = "/cinemas/deleteCinema/{id}")
    public String deleteCinema(@PathVariable int id,HttpSession ses) throws UserNotFoundException, NotAdminException {
        if(!isLogged(ses)) {
            throw new UserNotFoundException("You need to be logged to have that functionality");
        }
        int userId = (int) ses.getAttribute("LoggedUser");
        if(userService.getById(userId).getRoleId()!=2){
            throw new NotAdminException("Only admins can remove cinemas");
        }
        cinemaService.removeCinema(id);
        return "Cinema succesfully deleted";
    }

    @PutMapping(value = "/cinemas/id/{id}")
    public CinemaDTO editCinema(@PathVariable int id,@RequestBody CinemaDTO cinemaDTO,HttpSession ses) throws UserNotFoundException, NotAdminException, BadCredentialsException, MissingFieldException {
        if(!isLogged(ses)) {
            throw new UserNotFoundException("You need to be logged to have that functionality");
        }
        int userId = (int) ses.getAttribute("LoggedUser");
        if(userService.getById(userId).getRoleId()!=2){
            throw new NotAdminException("Only admins can add edit cinemas");
        }
        if(!validateCinemaFields(cinemaDTO.getCity(),cinemaDTO.getName())){
            throw new MissingFieldException("Please fill all requested fields");
        }
       return cinemaService.editCinema(cinemaDTO,id);
    }

    private boolean validateCinemaFields(String city,String name) throws MissingFieldException, BadCredentialsException {
        if(city.isBlank() || name.isBlank()){
            throw new MissingFieldException("Please fill all necessary fields");
        }
        if(city.length()>20 || city.length()<3){
            throw new BadCredentialsException("City names must be with at least 3 letters or max with 20");
        }
        if(name.length()>25){
            throw new BadCredentialsException("Name can contain maximum 25 letters");
        }
        return true;
    }



}
