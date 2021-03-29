package com.finals.kinoarena.Service;

import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Exceptions.NotFoundException;
import com.finals.kinoarena.Model.DTO.HallDTO;
import com.finals.kinoarena.Model.Entity.Cinema;
import com.finals.kinoarena.Model.Entity.Hall;
import com.finals.kinoarena.Model.Repository.CinemaRepository;
import com.finals.kinoarena.Model.Repository.HallRepository;
import com.finals.kinoarena.Model.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HallService extends AbstractService {

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

    public HallDTO addHall(HallDTO hallDTO, int userId) throws BadRequestException {
        if (!isAdmin(userId)) {
            throw new BadRequestException("Only admins can remove cinemas");
        }
        Optional<Cinema> sCinema = cinemaRepository.findById(hallDTO.getCinema().getId());
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

    public void removeHall(int cinemaId, int hallId, int userId) throws BadRequestException {
        if (!isAdmin(userId)) {
            throw new BadRequestException("Only admins can remove cinemas");
        }
        Optional<Cinema> sCinema = cinemaRepository.findById(cinemaId);
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema is not found");
        }
        Optional<Hall> sHall = hallRepository.findById(hallId);
        if (!cinemaHasHall(sCinema.get(), sHall)) {
            throw new NotFoundException("No hall with that id in this cinema");
        }
        hallRepository.deleteById(hallId);
    }

    public HallDTO editHall(HallDTO hallDTO, int cinemaID, int hallId, int userId) throws BadRequestException {
        if (!isAdmin(userId)) {
            throw new BadRequestException("Only admins can remove cinemas");
        }
        Optional<Cinema> sCinema = cinemaRepository.findById(cinemaID);
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
        return cinemaId == hallRepository.findByNumber(hallNumber).getCinema().getId();
    }
}
