package com.finals.kinoarena.Controller;

import com.finals.kinoarena.DAO.SeatDAO;
import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Exceptions.UnauthorizedException;
import com.finals.kinoarena.Model.DTO.HallDTO;
import com.finals.kinoarena.Model.Entity.User;
import com.finals.kinoarena.Service.HallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Component
@RestController
public class HallContoller extends AbstractController {


    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private HallService hallService;

    @GetMapping(value = "/hall/{hall_id}")
    public HallDTO getHallById(@PathVariable(name = "hall_id") int hallId) {
        return hallService.getHallById(hallId);
    }

    @PutMapping(value = "/hall")
    public HallDTO addHall(@RequestBody HallDTO hallDTO, HttpSession ses) throws BadRequestException, UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        if (hallDTO.getCapacity() < 50) {
            throw new BadRequestException("Hall capacity must be at least 50");
        }
        return hallService.addHall(hallDTO, user.getId());
    }

    @DeleteMapping(value = "/cinema/{cinema_id}/hall/{hallId}")
    public String deleteHall(@PathVariable(name = "cinema_id") int cinemaId, @PathVariable int hallId, HttpSession ses) throws BadRequestException, UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        int userId = user.getId();
        hallService.removeHall(cinemaId, hallId, userId);
        return "Hall successfully removed";
    }

    @PostMapping(value = "/cinema/{cinema_id}/hall/{hallId}")
    public HallDTO editHall(@PathVariable(name = "cinema_id") int cinemaId, @PathVariable int hallId, @RequestBody HallDTO hallDTO, HttpSession ses) throws BadRequestException, UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        int userId = user.getId();
        return hallService.editHall(hallDTO, cinemaId, hallId, userId);
    }
}


