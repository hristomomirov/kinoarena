package com.finals.kinoarena.service;

import com.finals.kinoarena.exceptions.*;
import com.finals.kinoarena.model.DTO.CinemaDTO;
import com.finals.kinoarena.model.DTO.CinemaWithoutHallDTO;
import com.finals.kinoarena.model.entity.Cinema;
import com.finals.kinoarena.model.repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CinemaService extends AbstractService {

    @Autowired
    private CinemaRepository cinemaRepository;

    public List<CinemaWithoutHallDTO> getAllCinemas() throws NotFoundException {
        List<Cinema> cinemas = cinemaRepository.findAll();
        if (cinemas.isEmpty()) {
            throw new NotFoundException("No found cinemas");
        }
        List<CinemaWithoutHallDTO> cinemaWithoutHallDTOS = new ArrayList<>();
        for (Cinema c : cinemas) {
            cinemaWithoutHallDTOS.add(new CinemaWithoutHallDTO(c));
        }
        return cinemaWithoutHallDTOS;
    }

    public CinemaDTO getCinemaById(int cinemaId) {
        Optional<Cinema> sCinema = cinemaRepository.findById(cinemaId);
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema not found");
        }
        return new CinemaDTO(sCinema.get());
    }
//TODO prob can be done in DAO
    public List<CinemaWithoutHallDTO> getAllCinemasByCity(String city) throws NotFoundException {
        List<Cinema> cinemas = cinemaRepository.findAllByCity(city);
        if (cinemas.isEmpty()) {
            throw new NotFoundException("No found cinemas in this city");
        }
        List<CinemaWithoutHallDTO> cinemaWithoutHallDTOS = new ArrayList<>();
        for (Cinema c : cinemas) {
            cinemaWithoutHallDTOS.add(new CinemaWithoutHallDTO(c));
        }
        return cinemaWithoutHallDTOS;

    }

    public CinemaDTO addCinema(CinemaDTO cinemaDTO, int userId) throws BadRequestException, UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can remove cinemas");
        }
        if (cinemaExists(cinemaDTO)) {
            throw new BadRequestException("There is already a cinema with that name in that city");
        }
      return new CinemaDTO(cinemaRepository.save(new Cinema(cinemaDTO.getName(),cinemaDTO.getCity()))); // работи!
    }

    public boolean cinemaExists(CinemaDTO cinemaDTO) {
        List<Cinema> cinemas = cinemaRepository.findAllByCity(cinemaDTO.getCity());
        for (Cinema c : cinemas) {
            if (c.getName().equals(cinemaDTO.getName())) {
                return true;
            }
        }
        return false;
    }

    public CinemaDTO removeCinema(int cinemaId, int userId) throws UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can remove cinemas");
        }
        CinemaDTO cinemaForDelete = getCinemaById(cinemaId);
        cinemaRepository.deleteById(cinemaId);
        return cinemaForDelete;
    }

    public CinemaDTO editCinema(CinemaDTO cinemaDTO, int id, int userId) throws BadRequestException, UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can remove cinemas");
        }
        Optional<Cinema> sCinema = cinemaRepository.findById(id);
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema is not found");
        }
        Cinema cinema = sCinema.get();
        if (cinema.getName().equals(cinemaDTO.getName()) && cinema.getCity().equals(cinemaDTO.getCity())) {
            throw new BadRequestException("You need to change the fields for an edit");
        }
        cinema.setCity(cinemaDTO.getCity());
        cinema.setName(cinemaDTO.getName());
        cinemaRepository.save(cinema);
        return new CinemaDTO(cinemaRepository.findById(id).get());
    }
}
