package com.finals.kinoarena.Service;

import com.finals.kinoarena.DAO.SeatDAO;
import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Exceptions.NotFoundException;
import com.finals.kinoarena.Model.DTO.AddProjectionDTO;
import com.finals.kinoarena.Model.DTO.GenreDTO;
import com.finals.kinoarena.Model.DTO.HalfProjectionDTO;
import com.finals.kinoarena.Model.DTO.ProjectionDTO;
import com.finals.kinoarena.Model.Entity.Cinema;
import com.finals.kinoarena.Model.Entity.Genre;
import com.finals.kinoarena.Model.Entity.Hall;
import com.finals.kinoarena.Model.Entity.Projection;
import com.finals.kinoarena.Model.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.finals.kinoarena.Model.Repository.UserRepository;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProjectionService extends AbstractService {

    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private SeatDAO seatDAO;


    public HalfProjectionDTO getProjectionById(int id) {
        Optional<Projection> sProjection = projectionRepository.findById(id);
        if (sProjection.isEmpty()) {
            throw new NotFoundException("Projection does not exist");
        }
        return new HalfProjectionDTO(sProjection.get());
    }

    public HalfProjectionDTO addProjection(AddProjectionDTO addProjectionDTO, int userId, int hallId) throws BadRequestException, SQLException {
        Optional<Hall> hall = hallRepository.findById(hallId);
        if (!isAdmin(userId)) {
            throw new BadRequestException("Only admins can add projections");
        }
        if (!projectionValidation(addProjectionDTO, hall)) {
            throw new BadRequestException("There is already a hall with that number in that cinema");
        }
        addProjectionDTO.setHall(hall.get());
        Projection projection = new Projection(addProjectionDTO);
        projection.setHall(hall.get());
        HalfProjectionDTO halfProjectionDTO = new HalfProjectionDTO(projectionRepository.save(projection));
        seatDAO.addFreeSeats(halfProjectionDTO.getId(), halfProjectionDTO.getHall().getCapacity());
        return halfProjectionDTO;
    }

    public List<Integer> getFreePlaces(int id) throws BadRequestException, SQLException {
        Optional<Projection> sProjection = projectionRepository.findById(id);
        if (sProjection.isEmpty()) {
            throw new BadRequestException("Projection does not exist");
        }
        return seatDAO.getFreeSeatsForProjection(id);
    }

    private boolean projectionValidation(AddProjectionDTO addProjectionDTO, Optional<Hall> hall) throws BadRequestException {
        if (hall.isEmpty()) {
            throw new NotFoundException("Hall not found");
        }
        List<Projection> projections = projectionRepository.findByHall(hall.get());
        for (Projection p : projections) {
            if (isBetween(p.getStartAt(), p.getEndAt(), addProjectionDTO.getStartAt())) {
                throw new BadRequestException("There is already a projection during this time in the hall");
            }
        }
        return true;
    }

    private boolean isBetween(LocalDateTime start, LocalDateTime end, LocalDateTime date) {
        return start.compareTo(date) * date.compareTo(end) >= 0;
    }

    public List<HalfProjectionDTO> getProjectionByGenre(int id) {
        List<Projection> projections = projectionRepository.findByGenre_Id(id);
        if (projections.isEmpty()) {
            throw new NotFoundException("No movies with this genre found");
        }
        List<HalfProjectionDTO> projectionDTOS = new ArrayList<>();
        for (Projection p : projections
        ) {
            projectionDTOS.add(new HalfProjectionDTO(p));
        }
        return projectionDTOS;
    }

    public List<GenreDTO> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        if (genres.isEmpty()) {
            throw new NotFoundException("No found genres");
        }
        List<GenreDTO> genreDTOS = new ArrayList<>();
        for (Genre g : genres
        ) {
            genreDTOS.add(new GenreDTO(g));
        }
        return genreDTOS;
    }

    public void removeProjection(int id, int userId) throws BadRequestException {
        if (userRepository.findById(userId).get().getRoleId() != 2) {
            throw new BadRequestException("Only admins can remove cinemas");
        }
        Optional<Projection> sProjection = projectionRepository.findById(id);
        if (sProjection.isEmpty()) {
            throw new NotFoundException("No projection with that id");
        }
        projectionRepository.deleteById(id);
    }

    public List<HalfProjectionDTO> getProjectionByCinema(int id) {
        List<Projection> projections = new ArrayList<>();
        Optional<Cinema> sCinema = cinemaRepository.findById(id);
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema is not found");
        }
        for (Hall h : sCinema.get().getHalls()) {
            projections.addAll(projectionRepository.findByHall_id(h.getId()));
        }
        if (projections.isEmpty()) {
            throw new NotFoundException("No projections found for this cinema");
        }
        List<HalfProjectionDTO> projectionDTOS = new ArrayList<>();
        for (Projection p : projections) {
            projectionDTOS.add(new HalfProjectionDTO(p));
        }
        return projectionDTOS;
    }

    public List<ProjectionDTO> getProjectionByCity(String city) {
        List<Projection> projections = new ArrayList<>();
        List<Cinema> cinemas = cinemaRepository.findByCity(city);
        if (cinemas.isEmpty()) {
            throw new NotFoundException("Cinema is not in this city");
        }
        for (Cinema c : cinemas) {
            for (Hall h : c.getHalls()) {
                projections.addAll(projectionRepository.findByHall_id(h.getId()));
            }
        }
        if (projections.isEmpty()) {
            throw new NotFoundException("No projections found for this city");
        }
        List<ProjectionDTO> projectionDTOS = new ArrayList<>();
        for (Projection p : projections) {
            projectionDTOS.add(new ProjectionDTO(p));
        }
        return projectionDTOS;
    }

    public HalfProjectionDTO editProjection(int userId, AddProjectionDTO addProjectionDTO,int projId) throws BadRequestException {
        if (!isAdmin(userId)) {
            throw new BadRequestException("Only admins can edit projections");
        }
        Optional<Hall> hall=hallRepository.findById(addProjectionDTO.getHallId());
        if(!projectionValidation(addProjectionDTO,hall)){
            throw new BadRequestException("The edit of this projection is in constrain to the time or hall");
        }
        Projection p = projectionRepository.findById(projId).get();
        p.setHall(hall.get());
        p.setTitle(addProjectionDTO.getTitle());
        p.setLength(addProjectionDTO.getLength());
        p.setDescription(addProjectionDTO.getDescription());
        p.setAgeRestriction(addProjectionDTO.getAgeRestriction());
        p.setGenre(addProjectionDTO.getGenre());
        p.setStartAt(addProjectionDTO.getStartAt());
        p.setEndAt(addProjectionDTO.getStartAt().plusMinutes(addProjectionDTO.getLength()));
        HalfProjectionDTO halfProjectionDTO = new HalfProjectionDTO(projectionRepository.save(p));
        return halfProjectionDTO;
    }
}
