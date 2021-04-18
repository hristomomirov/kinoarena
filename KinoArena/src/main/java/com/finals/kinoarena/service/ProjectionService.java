package com.finals.kinoarena.service;

import com.finals.kinoarena.model.entity.*;
import com.finals.kinoarena.util.exceptions.BadRequestException;
import com.finals.kinoarena.util.exceptions.NotFoundException;
import com.finals.kinoarena.util.exceptions.UnauthorizedException;
import com.finals.kinoarena.model.DTO.*;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectionService extends AbstractService {

    public ResponseProjectionDTO getProjectionById(int id) {
        Optional<Projection> sProjection = projectionRepository.findById(id);
        if (sProjection.isEmpty()) {
            throw new NotFoundException("Projection does not exist");
        }
        return new ResponseProjectionDTO(sProjection.get());
    }

    public ResponseProjectionDTO addProjection(RequestProjectionDTO requestProjectionDTO, int userId) throws BadRequestException, UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can add projections");
        }
        Optional<Hall> sHall = hallRepository.findById(requestProjectionDTO.getHallId());
        if (sHall.isEmpty()) {
            throw new NotFoundException("Hall not found");
        }
        Hall hall = sHall.get();
        if (!projectionValidation(requestProjectionDTO, hall)) {
            throw new BadRequestException("There is already a projection during this time in the hall");
        }
        Optional<Movie> sMovie = movieRepository.findById(requestProjectionDTO.getMovieId());
        if (sMovie.isEmpty()) {
            throw new NotFoundException("Movie with that Id does not exist");
        }
        Movie movie = sMovie.get();
        Projection projection = Projection.builder()
                .movie(movie)
                .hall(hall)
                .startAt(requestProjectionDTO.getStartAt())
                .endAt(requestProjectionDTO.getStartAt().plusMinutes(movie.getLength()))
                .reservedSeats(new ArrayList<>())
                .build();
        return new ResponseProjectionDTO(projectionRepository.save(projection));
    }

    public List<Integer> getFreePlaces(int projectionId) throws BadRequestException {
        Optional<Projection> sProjection = projectionRepository.findById(projectionId);
        if (sProjection.isEmpty()) {
            throw new BadRequestException("Projection does not exist");
        }
        return seatDAO.getReservedSeats(projectionId);
    }

    private boolean projectionValidation(RequestProjectionDTO requestProjectionDTO, Hall hall) throws BadRequestException {
        List<Projection> projections = projectionRepository.findByHall(hall);
        for (Projection p : projections) {
            if (isBetween(p.getStartAt(), p.getEndAt(), requestProjectionDTO.getStartAt())) {
                throw new BadRequestException("There is already a projection during this time");
            }
        }
        return true;
    }

    // работи inclussive
    private boolean isBetween(LocalDateTime start, LocalDateTime end, LocalDateTime date) {
        return start.compareTo(date) * date.compareTo(end) >= 0;
    }

    public ResponseProjectionDTO removeProjection(int projectionId, int userId) throws UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can remove projections");
        }
        Optional<Projection> sProjection = projectionRepository.findById(projectionId);
        if (sProjection.isEmpty()) {
            throw new NotFoundException("No projection with that id");
        }
        ResponseProjectionDTO deletedProjection = new ResponseProjectionDTO(sProjection.get());
        projectionRepository.deleteById(projectionId);
        return deletedProjection;

    }

    public List<ResponseProjectionDTO> getProjectionByCinema(int cinemaId) {
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
        List<ResponseProjectionDTO> projectionDTOS = new ArrayList<>();
        for (Projection p : projections) {
            projectionDTOS.add(new ResponseProjectionDTO(p));
        }
        return projectionDTOS;
    }

    public List<ResponseProjectionDTO> getProjectionByCity(String city) {
        List<Projection> projections = new ArrayList<>();
        List<Cinema> cinemas = cinemaRepository.findAllByCity(city);
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
        List<ResponseProjectionDTO> projectionDTOS = new ArrayList<>();
        for (Projection p : projections) {
            projectionDTOS.add(new ResponseProjectionDTO(p));
        }
        return projectionDTOS;
    }

    public ResponseProjectionDTO editProjection(int userId, RequestProjectionDTO requestProjectionDTO, int projectionId) throws BadRequestException, UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can edit projections");
        }
        Optional<Hall> sHall = hallRepository.findById(requestProjectionDTO.getHallId());
        if (sHall.isEmpty()) {
            throw new NotFoundException("Hall not found");
        }
        Hall hall = sHall.get();
        if (!projectionValidation(requestProjectionDTO, hall)) {
            throw new BadRequestException("There is already a projection during this time in the hall");
        }
        Optional<Movie> sMovie = movieRepository.findById(requestProjectionDTO.getMovieId());
        if (sMovie.isEmpty()) {
            throw new NotFoundException("Movie with that id does not exist");
        }
        Projection projection = projectionRepository.findById(projectionId).get();
        projection.setHall(sHall.get());
        projection.setMovie(sMovie.get());
        projection.setStartAt(requestProjectionDTO.getStartAt());
        projection.setEndAt(requestProjectionDTO.getStartAt().plusMinutes(projection.getMovie().getLength()));
        return new ResponseProjectionDTO(projectionRepository.save(projection));
    }

    public List<ResponseProjectionDTO> getAllProjectionsByGenre(int genre_id) {
        List<Movie> sMovies = movieRepository.findAllByGenreId(genre_id);
        if (sMovies.isEmpty()) {
            throw new NotFoundException("There are no movies with that genre");
        }
        List<Projection> sProjections = new ArrayList<>();
        for (Movie m : sMovies) {
            sProjections.addAll(projectionRepository.findByMovie_id(m.getId()));
        }
        if (sProjections.isEmpty()) {
            throw new NotFoundException("There are no projections with that genre");
        }
        List<ResponseProjectionDTO> projectionDTOS = new ArrayList<>();
        for (Projection p : sProjections) {
            projectionDTOS.add(new ResponseProjectionDTO(p));
        }
        return projectionDTOS;
    }

    public List<GenreDTO> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        if (genres.isEmpty()) {
            throw new NotFoundException("No found genres");
        }
        List<GenreDTO> genreDTOS = new ArrayList<>();
        for (Genre g : genres) {
            genreDTOS.add(new GenreDTO(g));
        }
        return genreDTOS;
    }
}
