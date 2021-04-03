package com.finals.kinoarena.service;

import com.finals.kinoarena.exceptions.BadRequestException;
import com.finals.kinoarena.exceptions.NotFoundException;
import com.finals.kinoarena.exceptions.UnauthorizedException;
import com.finals.kinoarena.model.DTO.HallDTO;
import com.finals.kinoarena.model.entity.Cinema;
import com.finals.kinoarena.model.entity.Hall;
import com.finals.kinoarena.model.repository.CinemaRepository;
import com.finals.kinoarena.model.repository.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HallService extends com.finals.kinoarena.service.AbstractService {

    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private CinemaRepository cinemaRepository;

    public HallDTO getHallById(int id) {
        Optional<Hall> sHall = hallRepository.findById(id);
        if (sHall.isEmpty()) {
            throw new NotFoundException("Hall not found");
        }
        return new HallDTO(sHall.get());
    }

    public HallDTO addHall(HallDTO hallDTO, int userId) throws BadRequestException, UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can add halls");
        }
        Optional<Cinema> sCinema = cinemaRepository.findById(hallDTO.getCinemaId());
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema is not found");
        }
        Cinema cinema = sCinema.get();
        if (cinemaHasHall(cinema.getId(), hallDTO.getNumber())) {
            throw new BadRequestException("There is already a hall with that number in that cinema");
        }
        Hall hall = new Hall(hallDTO);
        hall.setCinema(cinema);
        return new HallDTO(hallRepository.save(hall));
    }

    public HallDTO removeHall(int cinemaId, int hallId, int userId) throws UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can remove halls");
        }
        Optional<Cinema> sCinema = cinemaRepository.findById(cinemaId);
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema is not found");
        }
        Optional<Hall> sHall = hallRepository.findById(hallId);
        if (!cinemaHasHall(sCinema.get(), sHall)) {
            throw new NotFoundException("No hall with that id in this cinema");
        }
        HallDTO deletedHall = new HallDTO(sHall.get());
        hallRepository.deleteById(hallId);
        return deletedHall;
    }

    public HallDTO editHall(HallDTO hallDTO, int cinemaId, int hallId, int userId) throws BadRequestException, UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can edit halls");
        }
        Optional<Cinema> sCinema = cinemaRepository.findById(cinemaId);
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema is not found");
        }
        Optional<Hall> sHall = hallRepository.findById(hallId);
        if (!cinemaHasHall(sCinema.get(), sHall)) {
            throw new NotFoundException("No hall with that id in this cinema");
        }
        Hall hall = sHall.get();
        if (hall.getNumber() == hallDTO.getNumber() && hall.getCapacity() == hallDTO.getCapacity()) {
            throw new BadRequestException("You need to change the values for an edit");
        }
        hall.setNumber(hallDTO.getNumber());
        hall.setCapacity(hallDTO.getCapacity());
        return new HallDTO(hallRepository.save(hall));
    }

    private boolean cinemaHasHall(Cinema cinema, Optional<Hall> hall) {
        return hall.isPresent() && cinema.getHalls().contains(hall.get());
    }

    private boolean cinemaHasHall(int cinemaId, int hallNumber) {
        List<Hall> halls = hallRepository.findNumberByCinemaId(cinemaId);
        for (Hall h : halls) {
            if (h.getNumber() == hallNumber){
                return true;
            }
        }
        return false;
    }
}
