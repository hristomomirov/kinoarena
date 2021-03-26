package com.finals.kinoarena.Controller;

import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Exceptions.CinemaAlreadyExistException;
import com.finals.kinoarena.Model.DTO.CinemaDTO;
import com.finals.kinoarena.Model.DTO.HallDTO;
import com.finals.kinoarena.Service.CinemaService;
import com.finals.kinoarena.Service.HallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Component
@RestController
public class HallContoller extends AbstractController {

    private static final String LOGGED_USER = "LoggedUser";
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private HallService hallService;
    @Autowired
    private CinemaService cinemaService;

    @GetMapping(value="/hall/{id}")
    public HallDTO getHallbyId(@PathVariable int id){
       return hallService.getHallbyId(id);
    }

    @PutMapping(value ="/hall" )
      public HallDTO addHall(@RequestBody HallDTO hallDTO, HttpSession ses) throws BadRequestException  {
        if (!sessionManager.isLogged(ses)) {
            throw new BadRequestException("You need to be logged to have that functionality");
        }
        int userId = (int) ses.getAttribute(LOGGED_USER);
        if (!validateNewHall(hallDTO.getCapacity())) {
            throw new BadRequestException("Please fill all requested fields");
        }
        return hallService.addHall(hallDTO,userId);
    }

    @DeleteMapping(value = "/cinema/{id}/hall/{hallId}")
    public String deleteHall(@PathVariable int id,@PathVariable int hallId,HttpSession ses) throws BadRequestException {
        if (!sessionManager.isLogged(ses)) {
            throw new BadRequestException("You need to be logged to have that functionality");
        }
        int userId = (int) ses.getAttribute(LOGGED_USER);
        hallService.removeHall(id,hallId,userId);
        return "Hall successfully removed";
    }

    @PutMapping(value = "/cinema/{id}/hall/{hallId}")
    public HallDTO edintHall(@PathVariable int id,@PathVariable int hallId,@RequestBody HallDTO hallDTO, HttpSession ses) throws BadRequestException {
        if (!sessionManager.isLogged(ses)) {
            throw new BadRequestException("You need to be logged to have that functionality");
        }
        int userId = (int) ses.getAttribute(LOGGED_USER);
        return hallService.editHall(hallDTO,id,hallId,userId);
    }

    private boolean validateNewHall(int capacity) throws BadRequestException {
        if(capacity<20 || capacity> 250){
            throw new BadRequestException("Capacity must be between 20 and 250");
        }
        return true;
    }

}
