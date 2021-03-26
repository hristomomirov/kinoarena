package com.finals.kinoarena.Controller;

import com.finals.kinoarena.Model.DTO.ProjectionDTO;
import com.finals.kinoarena.Service.CinemaService;
import com.finals.kinoarena.Service.ProjectionService;
import com.finals.kinoarena.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

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


    @GetMapping(value = "/projections/{id}")
    public ProjectionDTO getProjectionById(@PathVariable int id) throws SQLException {
        return projectionService.getProjectionById(id);
    }

}
