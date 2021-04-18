package com.finals.kinoarena.service;

import com.finals.kinoarena.model.DTO.RequestCinemaDTO;
import com.finals.kinoarena.util.exceptions.*;
import com.finals.kinoarena.model.DTO.ResponseCinemaDTO;
import com.finals.kinoarena.model.entity.Cinema;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CinemaService extends AbstractService {

    public List<ResponseCinemaDTO> getAllCinemas() throws NotFoundException {
        List<Cinema> cinemas = cinemaRepository.findAll();
        if (cinemas.isEmpty()) {
            throw new NotFoundException("No cinemas found");
        }
        List<ResponseCinemaDTO> responseCinemaDTOS = new ArrayList<>();
        for (Cinema c : cinemas) {
            responseCinemaDTOS.add(new ResponseCinemaDTO(c));
        }
        return responseCinemaDTOS;
    }

    public ResponseCinemaDTO getCinemaById(int cinemaId) {
        Optional<Cinema> sCinema = cinemaRepository.findById(cinemaId);
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema not found");
        }
        return new ResponseCinemaDTO(sCinema.get());
    }

    public List<ResponseCinemaDTO> getAllCinemasByCity(String city) throws NotFoundException {
        List<Cinema> cinemas = cinemaRepository.findAllByCity(city);
        if (cinemas.isEmpty()) {
            throw new NotFoundException("No found cinemas in this city");
        }
        List<ResponseCinemaDTO> responseCinemaDTOS = new ArrayList<>();
        for (Cinema c : cinemas) {
            responseCinemaDTOS.add(new ResponseCinemaDTO(c));
        }
        return responseCinemaDTOS;
    }

    public ResponseCinemaDTO addCinema(RequestCinemaDTO requestCinemaDTO, int userId) throws BadRequestException, UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can add cinemas");
        }
        Cinema sCinema = cinemaRepository.findByCityAndName(requestCinemaDTO.getCity(), requestCinemaDTO.getName());
        if (sCinema != null) {
            throw new BadRequestException("There is already a cinema with that name in that city");
        }
        Cinema cinema = Cinema.builder()
                .name(requestCinemaDTO.getName())
                .city(requestCinemaDTO.getCity())
                .halls(new ArrayList<>())
                .build();
      return new ResponseCinemaDTO(cinemaRepository.save(cinema));
    }

    public ResponseCinemaDTO removeCinema(int cinemaId, int userId) throws UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can remove cinemas");
        }
        Optional<Cinema> sCinema = cinemaRepository.findById(cinemaId);
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema does not exist");
        }
        ResponseCinemaDTO cinemaForDelete = new ResponseCinemaDTO(sCinema.get());
        cinemaRepository.delete(sCinema.get());
        return cinemaForDelete;
    }

    public ResponseCinemaDTO editCinema(RequestCinemaDTO requestCinemaDTO, int cinemaId, int userId) throws BadRequestException, UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can edit cinemas");
        }
        Optional<Cinema> sCinema = cinemaRepository.findById(cinemaId);
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema does not exist");
        }
        Cinema cinema = sCinema.get();
        if (cinema.getName().equals(requestCinemaDTO.getName()) && cinema.getCity().equals(requestCinemaDTO.getCity())) {
            throw new BadRequestException("You need to change the fields for an edit");
        }
        cinema.setCity(requestCinemaDTO.getCity());
        cinema.setName(requestCinemaDTO.getName());
        return new ResponseCinemaDTO(cinemaRepository.save(cinema));
    }

}
