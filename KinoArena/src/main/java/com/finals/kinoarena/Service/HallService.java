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

    @Autowired
    private UserRepository userRepository;

    public HallDTO getHallById(int id) {
        Optional<Hall> schrodingerHall = hallRepository.findById(id);
        if (schrodingerHall.isPresent()) {
            return new HallDTO(schrodingerHall.get());
        } else {
            throw new NotFoundException("Hall not found");
        }
    }

    public HallDTO addHall(HallDTO hallDTO, int userId) throws BadRequestException {
        if (userRepository.findById(userId).get().getRoleId() != 2) {
            throw new BadRequestException("Only admins can remove cinemas");
        }
        if (!hallValidation(hallDTO)) {
            throw new BadRequestException("There is already a hall with that number in that cinema");
        }
        Hall hall = new Hall(hallDTO);
        hall.setCinema(hallDTO.getCinema());
        hallRepository.save(hall);
        return new HallDTO(hall);
    }

    public void removeHall(int cinemaId, int hallId, int userId) throws BadRequestException {
        if (userRepository.findById(userId).get().getRoleId() != 2) {
            throw new BadRequestException("Only admins can remove cinemas");
        }
        Optional<Cinema> sCinema = cinemaRepository.findById(cinemaId);
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema is not found");
        }
        if (!cinemaHasHall(sCinema.get(), hallId)) {
            throw new NotFoundException("No hall with that id in this cinema");
        }
        hallRepository.deleteById(hallId);
    }

    public HallDTO editHall(HallDTO hallDTO, int cinemaID, int hallId, int userId) throws BadRequestException {
        if (userRepository.findById(userId).get().getRoleId() != 2) {
            throw new BadRequestException("Only admins can remove cinemas");
        }
        Optional<Cinema> sCinema = cinemaRepository.findById(cinemaID);
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema is not found");
        }
        if (!cinemaHasHall(sCinema.get(), hallId)) {
            throw new NotFoundException("No hall with that id in this cinema");
        }
        Optional<Hall> sHall = hallRepository.findById(hallId);

        Hall hall = sHall.get();
        if (hall.getNumber() == hallDTO.getNumber() && hall.getCapacity() == hallDTO.getCapacity()) {
            throw new BadRequestException("You need to change the fields for an edit");
        }
        hall.setNumber(hallDTO.getNumber());
        hall.setCapacity(hallDTO.getCapacity());
        hallRepository.save(hall);
        return new HallDTO(hallRepository.findById(hallId).get());
    }

    private boolean hallValidation(HallDTO hallDTO) throws BadRequestException {
        Optional<Cinema> sCinema = cinemaRepository.findById(hallDTO.getCinema().getId());
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema is not found");
        }
        for (Hall h : sCinema.get().getHalls()) {
            if (h.getNumber() == hallDTO.getNumber()) {
                return false;
            }
        }
        return true;
    }


    private boolean cinemaHasHall(Cinema c, int hallId) {
        for (Hall h : c.getHalls()) {
            if (h.getId() == hallId) {
                return true;
            }
        }
        return false;
    }
}
