package com.finals.kinoarena.service;

import com.finals.kinoarena.model.DTO.RequestCinemaDTO;
import com.finals.kinoarena.util.exceptions.*;
import com.finals.kinoarena.model.DTO.ResponseCinemaDTO;
import com.finals.kinoarena.model.entity.Cinema;
import com.finals.kinoarena.model.repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CinemaService extends AbstractService {

    public List<ResponseCinemaDTO> getAllCinemas() throws NotFoundException {
        List<Cinema> cinemas = cinemaRepository.findAll();
        if (cinemas.isEmpty()) {
            throw new NotFoundException("No found cinemas");
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
            throw new UnauthorizedException("Only admins can remove cinemas");
        }
        if (cinemaExists(requestCinemaDTO)) {
            throw new BadRequestException("There is already a cinema with that name in that city");
        }
      return new ResponseCinemaDTO(cinemaRepository.save(new Cinema(requestCinemaDTO.getName(), requestCinemaDTO.getCity()))); // работи!
    }

    public boolean cinemaExists(RequestCinemaDTO requestCinemaDTO) {
        List<Cinema> cinemas = cinemaRepository.findAllByCity(requestCinemaDTO.getCity());
        for (Cinema c : cinemas) {
            if (c.getName().equals(requestCinemaDTO.getName())) {
                return true;
            }
        }
        return false;
    }

    public ResponseCinemaDTO removeCinema(int cinemaId, int userId) throws UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can remove cinemas");
        }
        Optional<Cinema> sCinema = cinemaRepository.findById(cinemaId);
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema does not exist");
        }
        ResponseCinemaDTO cinemaForDelete = getCinemaById(cinemaId);
        cinemaRepository.deleteById(cinemaId);
        return cinemaForDelete;
    }

    public ResponseCinemaDTO editCinema(RequestCinemaDTO requestCinemaDTO, int cinemaId, int userId) throws BadRequestException, UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can remove cinemas");
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
        cinemaRepository.save(cinema);
        return new ResponseCinemaDTO(cinemaRepository.findById(cinemaId).get());
    }
}
