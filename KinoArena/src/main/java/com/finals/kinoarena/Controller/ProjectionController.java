package com.finals.kinoarena.Controller;

import com.finals.kinoarena.DAO.ProjectionDAO;
import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Exceptions.UnauthorizedException;
import com.finals.kinoarena.Model.DTO.AddProjectionDTO;
import com.finals.kinoarena.Model.DTO.ProjectionDTO;
import com.finals.kinoarena.Model.Entity.Genre;
import com.finals.kinoarena.Model.Entity.Projection;
import com.finals.kinoarena.Model.Entity.User;
import com.finals.kinoarena.Model.Repository.GenreRepository;
import com.finals.kinoarena.Service.CinemaService;
import com.finals.kinoarena.Service.ProjectionService;
import com.finals.kinoarena.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RestController
public class ProjectionController extends AbstractController {

    private static final String LOGGED_USER = "LoggedUser";
    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private ProjectionService projectionService;
    @Autowired
    private ProjectionDAO dao;
    @Autowired
    private GenreRepository genreRepository;


    @GetMapping(value = "/projections/{id}")
    public ProjectionDTO getProjectionById(@PathVariable int id) throws SQLException {
        return projectionService.getProjectionById(id);
    }

    @GetMapping(value = "/projections/{id}/places")
    public String getFreePlacesForProjection(@PathVariable int id) throws BadRequestException {
        return projectionService.getFreePlaces(id);
    }

    @PutMapping(value = "/hall/{hall_id}/projection")
    public ProjectionDTO addProjection(@RequestBody AddProjectionDTO addProjectionDTO, HttpSession ses, @PathVariable(name = "hall_id") int hallId) throws BadRequestException, UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        if (!validateNewProjection(addProjectionDTO)) {
            throw new BadRequestException("Please fill all requested fields");
        }
        return projectionService.addProjection(addProjectionDTO, user.getId(), hallId);
    }
    @PutMapping(value = "/seats")
    public void addNumber() throws SQLException {
        dao.addSeats();
    }

    private boolean validateNewProjection(AddProjectionDTO dto) throws BadRequestException {
        return validateTitle(dto.getTitle()) &&
                validateLength(dto.getLength()) &&
                validateDescription(dto.getDescription()) &&
                validateAgeRestriction(dto.getAgeRestriction()) &&
                validateGenre(dto) &&
                validateStartAt(dto);
    }

    private boolean validateStartAt(AddProjectionDTO dto) throws BadRequestException {
        if (dto.getTime().isBlank()){
            throw new BadRequestException("Please fill all requested fields");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dto.getTime(), formatter);
        if (dateTime.isAfter(LocalDateTime.now())) {
            dto.setStartAt(dateTime);
            return true;
        }
        throw new BadRequestException("Starting time must be after" + LocalDateTime.now().toString());
    }

    private boolean validateGenre(AddProjectionDTO dto) throws BadRequestException {
        List<Genre> g = genreRepository.findAllByType(dto.getType());
        if (g.isEmpty()) {
            throw new BadRequestException("Invalid genre");
        }
        dto.setGenre(g.get(0));
        return true;
    }

    private boolean validateAgeRestriction(int ageRestriction) throws BadRequestException {
        if (ageRestriction >= 3) {
            return true;
        }
        throw new BadRequestException("Age restriction cannot be less than 3");
    }

    private boolean validateDescription(String description) throws BadRequestException {
        if (!description.isBlank()) {
            return true;
        }
        throw new BadRequestException("Description cannot be empty");
    }

    private boolean validateLength(int length) throws BadRequestException {
        if (length > 60 && length <= 300) {
            return true;
        }
        throw new BadRequestException("Length must be between 60 and 300 minutes");
    }

    private boolean validateTitle(String title) throws BadRequestException {
        if (!title.isBlank() && title.length() <= 200) {
            return true;
        }
        throw new BadRequestException("Name cannot be empty or more than 200 characters");
    }



}
