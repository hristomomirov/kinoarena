package com.finals.kinoarena.controller;

import com.finals.kinoarena.model.DTO.RequestCinemaDTO;
import com.finals.kinoarena.util.exceptions.*;
import com.finals.kinoarena.model.entity.User;
import com.finals.kinoarena.service.CinemaService;
import com.finals.kinoarena.model.DTO.ResponseCinemaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


@Component
@RestController
public class CinemaController extends AbstractController  {

    @Autowired
    private CinemaService cinemaService;

    @GetMapping(value = "/cinemas")
    public List<ResponseCinemaDTO> getAllCinemas() throws NotFoundException {
        return cinemaService.getAllCinemas();
    }

    @GetMapping(value = "/cinema/{cinema_id}")
    public ResponseCinemaDTO getCinemaById(@PathVariable(name = "cinema_id") int cinema_Id) {
        return cinemaService.getCinemaById(cinema_Id);
    }

    @GetMapping(value = "/cinemas/city/{city}")
    public List<ResponseCinemaDTO> getAllCinemasByCity(@PathVariable String city) throws NotFoundException {
        return cinemaService.getAllCinemasByCity(city);
    }

    @PutMapping(value = "/cinemas")
    public ResponseCinemaDTO addCinema(@RequestBody RequestCinemaDTO requestCinemaDTO, HttpSession ses) throws BadRequestException, NotFoundException, UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        int userId = user.getId();
        if (!validateNewCinema(requestCinemaDTO.getCity(), requestCinemaDTO.getName())) {
            throw new BadRequestException("Please fill all requested fields");
        }
        return cinemaService.addCinema(requestCinemaDTO, userId);
    }

    @DeleteMapping(value = "/cinemas/{cinema_id}")
    public ResponseCinemaDTO deleteCinema(@PathVariable(name = "cinema_id") int cinemaId, HttpSession ses) throws UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        int userId = user.getId();
        return cinemaService.removeCinema(cinemaId, userId);

    }

    @PostMapping(value = "/cinemas/{cinema_id}")
    public ResponseCinemaDTO editCinema(@PathVariable(name = "cinema_id") int cinemaId, @RequestBody RequestCinemaDTO requestCinemaDTO, HttpSession ses) throws UnauthorizedException, BadRequestException {
        User user = sessionManager.getLoggedUser(ses);
        if (!validateNewCinema(requestCinemaDTO.getCity(), requestCinemaDTO.getName())) {
            throw new BadRequestException("Please fill all requested fields");
        }
        return cinemaService.editCinema(requestCinemaDTO, cinemaId, user.getId());
    }

    private boolean validateNewCinema(String city, String name) throws BadRequestException {
        if (city.isBlank() || name.isBlank()) {
            throw new BadRequestException("Please fill all necessary fields");
        }
        if (city.length() > 20 || city.length() < 3) {
            throw new BadRequestException("City names must be with at least 3 letters or max with 20");
        }
        if (name.length() > 25) {
            throw new BadRequestException("Name can contain maximum 25 letters");
        }
        return true;
    }
}
