package com.finals.kinoarena.Service;

import com.finals.kinoarena.DAO.ProjectionDAO;
import com.finals.kinoarena.DAO.TicketDAO;
import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Exceptions.MissingCinemasInDBException;
import com.finals.kinoarena.Exceptions.NotFoundException;
import com.finals.kinoarena.Model.DTO.CinemaDTO;
import com.finals.kinoarena.Model.DTO.GenreDTO;
import com.finals.kinoarena.Model.DTO.ProjectionDTO;
import com.finals.kinoarena.Model.Entity.Cinema;
import com.finals.kinoarena.Model.Entity.Genre;
import com.finals.kinoarena.Model.Entity.Hall;
import com.finals.kinoarena.Model.Entity.Projection;
import com.finals.kinoarena.Model.Repository.CinemaRepository;
import com.finals.kinoarena.Model.Repository.GenreRepository;
import com.finals.kinoarena.Model.Repository.ProjectionRepository;
import com.finals.kinoarena.Model.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    public ProjectionDTO getProjectionById(int id) throws SQLException {
        Optional<Projection> sProjection = projectionRepository.findById(id);
        if (sProjection.isEmpty()) {
            throw new NotFoundException("Projection does not exist");
        }
        return new ProjectionDTO(sProjection.get());
    }

    public String getFreePlaces(int id) throws BadRequestException {
        Optional<Projection> sProjection = projectionRepository.findById(id);
        if (sProjection.isEmpty()){
            throw new BadRequestException("Projection does not exist");
        }
        return sProjection.get().getFreePlaces().toString();
    }

    public List<ProjectionDTO> getProjectionByGenre(int id) {
        List<Projection> projections = projectionRepository.findByGenre_Id(id);
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
        if(!sProjection.isPresent()){
            throw new NotFoundException("No projection with that id");
        }
        projectionRepository.deleteById(id);
    }

    public List<ProjectionDTO> getProjectionByCinema(int id) {
        List<Projection> projections = new ArrayList<>();
        Optional<Cinema> sCinema = cinemaRepository.findById(id);
        if (!sCinema.isPresent()) {
            throw new NotFoundException("Cinema is not found");
        }
        for (Hall h:sCinema.get().getHalls()
             ) {
           projections.addAll(projectionRepository.findByHall_id(h.getId()));
        }

        if (projections.isEmpty()) {
            throw new NotFoundException("No projections found for this cinema");
        }
        List<ProjectionDTO> projectionDTOS = new ArrayList<>();
        for (Projection p : projections
        ) {
            projectionDTOS.add(new ProjectionDTO(p));
        }
        return projectionDTOS;
    }
}
