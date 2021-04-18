package com.finals.kinoarena.service;

import com.finals.kinoarena.model.DTO.ResponseHallDTO;
import com.finals.kinoarena.util.exceptions.BadRequestException;
import com.finals.kinoarena.util.exceptions.NotFoundException;
import com.finals.kinoarena.util.exceptions.UnauthorizedException;
import com.finals.kinoarena.model.DTO.RequestHallDTO;
import com.finals.kinoarena.model.entity.Cinema;
import com.finals.kinoarena.model.entity.Hall;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class HallService extends com.finals.kinoarena.service.AbstractService {

    public ResponseHallDTO getHallById(int id) {
        Optional<Hall> sHall = hallRepository.findById(id);
        if (sHall.isEmpty()) {
            throw new NotFoundException("Hall not found");
        }
        return new ResponseHallDTO(sHall.get());
    }

    public ResponseHallDTO addHall(RequestHallDTO requestHallDTO, int userId) throws BadRequestException, UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can add halls");
        }
        Optional<Cinema> sCinema = cinemaRepository.findById(requestHallDTO.getCinemaId());
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema is not found");
        }
        Cinema cinema = sCinema.get();
        if (cinemaHasHall(cinema,requestHallDTO.getNumber())) {
            throw new BadRequestException("There is already a hall with that number in that cinema");
        }
        Hall hall = Hall.builder()
                .number(requestHallDTO.getNumber())
                .capacity(requestHallDTO.getCapacity())
                .cinema(cinema)
                .projections(new ArrayList<>())
                .build();
        return new ResponseHallDTO(hallRepository.save(hall));
    }

    public ResponseHallDTO removeHall(int hallId, int userId) throws UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can remove halls");
        }
        Optional<Hall> sHall = hallRepository.findById(hallId);
        if (sHall.isEmpty()) {
            throw new NotFoundException("Hall does not exist");
        }
        ResponseHallDTO deletedHall = new ResponseHallDTO(sHall.get());
        hallRepository.deleteById(hallId);
        return deletedHall;
    }

    public ResponseHallDTO editHall(RequestHallDTO requestHallDTO, int hallId, int userId) throws BadRequestException, UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can edit halls");
        }
        Optional<Cinema> sCinema = cinemaRepository.findById(requestHallDTO.getCinemaId());
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema is not found");
        }
        Optional<Hall> sHall = hallRepository.findById(hallId);
        if (!cinemaHasHall(sCinema.get(), sHall)) {
            throw new NotFoundException("No hall with that id in this cinema");
        }
        Hall hall = sHall.get();
        if (hall.getNumber() == requestHallDTO.getNumber() && hall.getCapacity() == requestHallDTO.getCapacity()) {
            throw new BadRequestException("You need to change the values for an edit");
        }
        hall.setNumber(requestHallDTO.getNumber());
        hall.setCapacity(requestHallDTO.getCapacity());
        return new ResponseHallDTO(hallRepository.save(hall));
    }

    private boolean cinemaHasHall(Cinema cinema, Optional<Hall> hall) {
        return hall.isPresent() && cinema.getHalls().contains(hall.get());
    }

    private boolean cinemaHasHall(Cinema cinema, int hallNumber) {
        for (Hall h : cinema.getHalls()) {
            if (h.getNumber() == hallNumber){
                return true;
            }
        }
        return false;
    }
}
