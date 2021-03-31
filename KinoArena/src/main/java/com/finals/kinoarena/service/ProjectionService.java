package com.finals.kinoarena.service;

import com.finals.kinoarena.DAO.SeatDAO;
import com.finals.kinoarena.exceptions.BadRequestException;
import com.finals.kinoarena.exceptions.NotFoundException;
import com.finals.kinoarena.exceptions.UnauthorizedException;
import com.finals.kinoarena.model.DTO.*;
import com.finals.kinoarena.model.entity.Cinema;
import com.finals.kinoarena.model.entity.Hall;
import com.finals.kinoarena.model.entity.Movie;
import com.finals.kinoarena.model.entity.Projection;
import com.finals.kinoarena.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    private CinemaRepository cinemaRepository;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private SeatDAO seatDAO;
    @Autowired
    private MovieRepository movieRepository;


    public ResponseProjectionDTO getProjectionById(int id) {
        Optional<Projection> sProjection = projectionRepository.findById(id);
        if (sProjection.isEmpty()) {
            throw new NotFoundException("Projection does not exist");
        }
        return new ResponseProjectionDTO(sProjection.get());
    }
// TODO must be transaction , transactional doesnt work
    public ResponseProjectionDTO addProjection(AddProjectionDTO addProjectionDTO, int userId) throws BadRequestException, SQLException, UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can add projections");
        }
        Optional<Hall> sHall = hallRepository.findById(addProjectionDTO.getHallId());
        if (sHall.isEmpty()) {
            throw new NotFoundException("Hall not found");
        }
        Hall hall = sHall.get();
        if (!projectionValidation(addProjectionDTO, hall)) {
            throw new BadRequestException("There is already a projection during this time in the hall");
        }
        Optional<Movie> sMovie = movieRepository.findById(addProjectionDTO.getMovieId());
        if (sMovie.isEmpty()) {
            throw new NotFoundException("Movie with that id does not exist");
        }
        Projection projection = new Projection(addProjectionDTO);
        projection.setHall(hall);
        projection.setMovie(sMovie.get());
        projection.setEndAt(projection.getStartAt().plusMinutes(sMovie.get().getLength())); // proj start + movie.getLength
        ResponseProjectionDTO responseProjectionDTO = new ResponseProjectionDTO(projectionRepository.save(projection));
        seatDAO.addFreeSeats(responseProjectionDTO.getId(), responseProjectionDTO.getHall().getCapacity());
        return responseProjectionDTO;
    }

    public List<Integer> getFreePlaces(int id) throws BadRequestException, SQLException {
        Optional<Projection> sProjection = projectionRepository.findById(id);
        if (sProjection.isEmpty()) {
            throw new BadRequestException("Projection does not exist");
        }
        return seatDAO.getFreeSeatsForProjection(id);
    }

    private boolean projectionValidation(AddProjectionDTO addProjectionDTO, Hall hall) throws BadRequestException {
        List<Projection> projections = projectionRepository.findByHall(hall);
        for (Projection p : projections) {
            if (isBetween(p.getStartAt(), p.getEndAt(), addProjectionDTO.getStartAt())) {
                throw new BadRequestException("There is already a projection during this time");
            }
        }
        return true;
    }

    // работи inclussive
    private boolean isBetween(LocalDateTime start, LocalDateTime end, LocalDateTime date) {
        return start.compareTo(date) * date.compareTo(end) >= 0;
    }

    public ProjectionDTO removeProjection(int projectionId, int userId) throws UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can remove projections");
        }
        Optional<Projection> sProjection = projectionRepository.findById(projectionId);
        if (sProjection.isEmpty()) {
            throw new NotFoundException("No projection with that id");
        }
        ProjectionDTO deletedProjection = new ProjectionDTO(sProjection.get());
        projectionRepository.deleteById(projectionId);
        return deletedProjection;

    }

    public List<ProjectionDTO> getProjectionByCinema(int cinemaId) {
        List<Projection> projections = new ArrayList<>();
        Optional<Cinema> sCinema = cinemaRepository.findById(cinemaId);
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

    public List<ProjectionDTO> getProjectionByCity(String city) {
        List<Projection> projections = new ArrayList<>();
        List<Cinema> cinemas = cinemaRepository.findAllByCity(city);
        if (cinemas.isEmpty()) {
            throw new NotFoundException("Cinema is not in this city");
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

    public ResponseProjectionDTO editProjection(int userId, AddProjectionDTO addProjectionDTO, int projId) throws BadRequestException, UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can edit projections");
        }
        Optional<Hall> sHall = hallRepository.findById(addProjectionDTO.getHallId());
        if (sHall.isEmpty()) {
            throw new NotFoundException("Hall not found");
        }
        Hall hall = sHall.get();
        if (!projectionValidation(addProjectionDTO, hall)) {
            throw new BadRequestException("The edit of this projection is in constrain to the time or hall");
        }
        Optional<Movie> sMovie = movieRepository.findById(addProjectionDTO.getMovieId());
        if (sMovie.isEmpty()) {
            throw new NotFoundException("Movie with that id does not exist");
        }
        Projection p = projectionRepository.findById(projId).get();
        p.setHall(sHall.get());
        p.setMovie(sMovie.get());
        p.setStartAt(addProjectionDTO.getStartAt());
        p.setEndAt(addProjectionDTO.getStartAt().plusMinutes(p.getMovie().getLength()));
        return new ResponseProjectionDTO(projectionRepository.save(p));
    }

    public List<ProjectionDTO> getAllProjectionsByGenre(int genre_id) {
        List<Movie> sMovies = movieRepository.findAllByGenreId(genre_id);
        if (sMovies.isEmpty()) {
            throw new NotFoundException("There are no movies with that genre");
        }
        List<Projection> sProjections = new ArrayList<>();
        for (Movie m : sMovies
        ) {
            sProjections.addAll(projectionRepository.findByMovie_id(m.getId()));
        }
        if (sProjections.isEmpty()) {
            throw new NotFoundException("There are no projections with that genre");
        }
        List<ProjectionDTO> projectionDTOS = new ArrayList<>();
        for (Projection p : sProjections) {
            projectionDTOS.add(new ProjectionDTO(p));
        }
        return projectionDTOS;
    }
}
