package com.finals.kinoarena.controller;

import com.finals.kinoarena.model.DTO.ResponseHallDTO;
import com.finals.kinoarena.util.exceptions.BadRequestException;
import com.finals.kinoarena.util.exceptions.UnauthorizedException;
import com.finals.kinoarena.model.DTO.RequestHallDTO;
import com.finals.kinoarena.model.entity.User;
import com.finals.kinoarena.service.HallService;
import com.finals.kinoarena.util.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Component
@RestController
public class HallController extends AbstractController {


    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private HallService hallService;

    @GetMapping(value = "/hall/{hall_id}")
    public ResponseHallDTO getHallById(@PathVariable(name = "hall_id") int hallId) {
        return hallService.getHallById(hallId);
    }

    @PutMapping(value = "/halls")
    public ResponseHallDTO addHall(@RequestBody RequestHallDTO requestHallDTO, HttpSession ses) throws BadRequestException, UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        if (requestHallDTO.getCapacity() < 50) {
            throw new BadRequestException("Hall capacity must be at least 50");
        }
        return hallService.addHall(requestHallDTO, user.getId());
    }

    @DeleteMapping(value = "/hall/{hallId}")
    public ResponseHallDTO deleteHall(@PathVariable int hallId, HttpSession ses) throws UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        int userId = user.getId();
        return hallService.removeHall(hallId, userId);
    }

    @PostMapping(value = "/hall/{hallId}")
    public ResponseHallDTO editHall(@PathVariable int hallId, @RequestBody RequestHallDTO requestHallDTO, HttpSession ses) throws BadRequestException, UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        int userId = user.getId();
        if (requestHallDTO.getCapacity() < 50) {
            throw new BadRequestException("Hall capacity must be at least 50");
        }
        return hallService.editHall(requestHallDTO, hallId, userId);
    }
}


