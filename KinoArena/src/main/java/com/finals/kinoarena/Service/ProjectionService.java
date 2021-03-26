package com.finals.kinoarena.Service;

import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Exceptions.NotFoundException;
import com.finals.kinoarena.Model.DTO.AddProjectionDTO;
import com.finals.kinoarena.Model.DTO.GenreDTO;
import com.finals.kinoarena.Model.DTO.ProjectionDTO;
import com.finals.kinoarena.Model.Entity.Cinema;
import com.finals.kinoarena.Model.Entity.Genre;
import com.finals.kinoarena.Model.Entity.Hall;
import com.finals.kinoarena.Model.Entity.Projection;
import com.finals.kinoarena.Model.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProjectionService extends AbstractService {
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GenreRepository genreRepository;


    public ProjectionDTO getProjectionById(int id) throws SQLException {
        Optional<Projection> sProjection = projectionRepository.findById(id);
        if (sProjection.isEmpty()) {
            throw new NotFoundException("Projection does not exist");
        }
        return new ProjectionDTO(sProjection.get());
    }

    //TODO
    public String getFreePlaces(int id) throws BadRequestException {
        Optional<Projection> sProjection = projectionRepository.findById(id);
        if (sProjection.isEmpty()) {
            throw new BadRequestException("Projection does not exist");
        }
        return null;
    }

    public ProjectionDTO addProjection(AddProjectionDTO addProjectionDTO, int userId, int hallId) throws BadRequestException {
        Optional<Hall> hall = hallRepository.findById(hallId);
        if (!isAdmin(userId)) {
            throw new BadRequestException("Only admins can remove cinemas");
        }
        if (!projectionValidation(addProjectionDTO, hall)) {
            throw new BadRequestException("There is already a hall with that number in that cinema");
        }
        addProjectionDTO.setHall(hall.get());
        Projection projection = new Projection(addProjectionDTO);
        projection.setHall(hall.get());
        return new ProjectionDTO(projectionRepository.save(projection));
    }

    public List<ProjectionDTO> getProjectionByCity(String city) {
        List<Projection> projections = new ArrayList<>();
        List<Cinema> cinemas = cinemaRepository.findByCity(city);
        if (cinemas.isEmpty()) {
            throw new NotFoundException("No found cinemas in this city");
        }
        for (Cinema c : cinemas) {
            for (Hall h : c.getHalls()) {
                projections.addAll(projectionRepository.findByHallId(h.getId()));
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

    public List<ProjectionDTO> getProjectionByGenre(int id) {
        List<Projection> projections = projectionRepository.findByGenreId(id);
        if (projections.isEmpty()) {
            throw new NotFoundException("No movies with this genre found");
        }
        List<ProjectionDTO> projectionDTOS = new ArrayList<>();
        for (Projection p : projections
        ) {
            projectionDTOS.add(new ProjectionDTO(p));
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

    public List<ProjectionDTO> getProjectionByCinema(int id) {
        List<Projection> projections = new ArrayList<>();
        Optional<Cinema> sCinema = cinemaRepository.findById(id);
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema is not found");
        }
        for (Hall h : sCinema.get().getHalls()) {
            projections.addAll(projectionRepository.findByHallId(h.getId()));
        }
        if (projections.isEmpty()) {
            throw new NotFoundException("No projections found for this cinema");
        }
        List<ProjectionDTO> projectionDTOS = new ArrayList<>();
        for (Projection p : projections) {
            projectionDTOS.add(new ProjectionDTO(p));
        }
        return projectionDTOS;
    }

    private boolean projectionValidation(AddProjectionDTO addProjectionDTO, Optional<Hall> hall) throws BadRequestException {
        if (hall.isEmpty()) {
            throw new NotFoundException("Hall not found");
        }
        List<Projection> projections = projectionRepository.findByHall(hall.get());
        for (Projection p : projections) {
            if (p.getStartAt().isEqual(addProjectionDTO.getStartAt()) || p.getEndAt().isAfter(addProjectionDTO.getStartAt())) {
                throw new BadRequestException("There is already a projection during this time in the hall");
            }
        }
        return true;
    }
}
