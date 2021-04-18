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
import javax.validation.Valid;
import java.util.List;


@Component
@RestController
public class CinemaController extends AbstractController {

    @Autowired
    private CinemaService cinemaService;

    @GetMapping(value = "/cinemas")
    public List<ResponseCinemaDTO> getAllCinemas() throws NotFoundException {
        return cinemaService.getAllCinemas();
    }

    @GetMapping(value = "/cinemas/{cinema_id}")
    public ResponseCinemaDTO getCinemaById(@PathVariable(name = "cinema_id") int cinema_Id) {
        return cinemaService.getCinemaById(cinema_Id);
    }

    @GetMapping(value = "/city/{city}/cinemas")
    public List<ResponseCinemaDTO> getAllCinemasByCity(@PathVariable String city) throws NotFoundException {
        return cinemaService.getAllCinemasByCity(city);
    }

    @PostMapping(value = "/cinemas")
    public ResponseCinemaDTO addCinema(@Valid @RequestBody RequestCinemaDTO requestCinemaDTO, HttpSession ses) throws BadRequestException, NotFoundException, UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        return cinemaService.addCinema(requestCinemaDTO, user.getId());
    }

    @DeleteMapping(value = "/cinemas/{cinema_id}")
    public ResponseCinemaDTO deleteCinema(@PathVariable(name = "cinema_id") int cinemaId, HttpSession ses) throws UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        return cinemaService.removeCinema(cinemaId, user.getId());

    }

    @PutMapping(value = "/cinemas/{cinema_id}")
    public ResponseCinemaDTO editCinema(@PathVariable(name = "cinema_id") int cinemaId,
                                        @Valid @RequestBody RequestCinemaDTO requestCinemaDTO, HttpSession ses) throws UnauthorizedException, BadRequestException {
        User user = sessionManager.getLoggedUser(ses);
        return cinemaService.editCinema(requestCinemaDTO, cinemaId, user.getId());
    }
}
