package com.finals.kinoarena.Controller;

import com.finals.kinoarena.DAO.ProjectionDAO;
import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Model.DTO.GenreDTO;
import com.finals.kinoarena.Model.DTO.ProjectionDTO;
import com.finals.kinoarena.Service.CinemaService;
import com.finals.kinoarena.Service.ProjectionService;
import com.finals.kinoarena.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@Component
@RestController
public class ProjectionController extends AbstractController{

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


    @GetMapping(value = "/projections/{id}")
    public ProjectionDTO getProjectionById(@PathVariable int id) throws SQLException {
        return projectionService.getProjectionById(id);
    }

    @GetMapping(value = "/projections/{id}/places")
    public String getFreePlacesForProjection(@PathVariable int id) throws BadRequestException {
        return projectionService.getFreePlaces(id);
    }

    @PutMapping(value = "/seats")
    public void addProjection() throws SQLException {
        dao.addSeats();
    }

    @GetMapping(value = "/genre/{id}")
    public List<ProjectionDTO> getProjectionsByGenre(@PathVariable int id){
        return projectionService.getProjectionByGenre(id);
    }

    @GetMapping(value = "/genres")
    public List<GenreDTO> getAllgenres(){
        return projectionService.getAllGenres();
    }

    @DeleteMapping(value = "/projections/{id}")
    public String deleteProjection(@PathVariable int id, HttpSession ses) throws BadRequestException {
        if (!sessionManager.isLogged(ses)) {
            throw new BadRequestException("You need to be logged to have that functionality");
        }
        int userId = (int) ses.getAttribute(LOGGED_USER);
        projectionService.removeProjection(id,userId);
        return "Projection succesfully deleted";
    }

    @GetMapping(value = "/cinema/{id}/projections")
    public List<ProjectionDTO> getAllProjectionsForCinema(@PathVariable int id){
        return projectionService.getProjectionByCinema(id);
    }





}
