package com.finals.kinoarena.controller;

import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Exceptions.UnauthorizedException;
import com.finals.kinoarena.Model.DTO.AddProjectionDTO;
import com.finals.kinoarena.Model.DTO.HalfProjectionDTO;
import com.finals.kinoarena.Model.DTO.ProjectionDTO;
import com.finals.kinoarena.Model.Entity.User;
import com.finals.kinoarena.Service.ProjectionService;
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

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private ProjectionService projectionService;

    @GetMapping(value = "/projections/{projection_id}")
    public HalfProjectionDTO getProjectionById(@PathVariable(name = "projection_id") int projectionId) {
        return projectionService.getProjectionById(projectionId);
    }

    @GetMapping(value = "/projections/{projection_id}/places")
    public List<Integer> getFreePlacesForProjection(@PathVariable(name = "projection_id") int projectionId) throws BadRequestException, SQLException {
        return projectionService.getFreePlaces(projectionId);
    }

    @PutMapping(value = "/hall/{hall_id}/projection")
    public HalfProjectionDTO addProjection(@RequestBody AddProjectionDTO addProjectionDTO, HttpSession ses,
                                           @PathVariable(name = "hall_id") int hallId) throws BadRequestException, UnauthorizedException, SQLException {
        User user = sessionManager.getLoggedUser(ses);
        if (!validateNewProjection(addProjectionDTO)) {
            throw new BadRequestException("Please fill all requested fields");
        }
        return projectionService.addProjection(addProjectionDTO, user.getId(), hallId);
    }

    @GetMapping(value = "/cinema/{cinema_id}/projections")
    public List<HalfProjectionDTO> getAllProjectionsForCinema(@PathVariable(name = "cinema_id") int cinemaId) {
        return projectionService.getProjectionByCinema(cinemaId);
    }

    @GetMapping(value = "/projections/city/{city}")
    public List<ProjectionDTO> getAllProjectionsForCinema(@PathVariable String city) {
        return projectionService.getProjectionByCity(city);
    }

    @PostMapping(value = "/projection/{projection_id}")
    public HalfProjectionDTO editProjection(@RequestBody AddProjectionDTO addProjectionDTO,HttpSession ses,
                                            @PathVariable(name = "projection_id") int projectionId) throws BadRequestException, UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        projectionService.getProjectionById(projectionId);//if does not exist this will throw exception
        if (!validateNewProjection(addProjectionDTO)) {
            throw new BadRequestException("Please fill all requested fields");
        }
        return projectionService.editProjection(user.getId(), addProjectionDTO,projectionId);
    }

    @DeleteMapping(value = "/projections/{projection_id}")
    public ProjectionDTO deleteProjection(@PathVariable(name = "projection_id") int projectionId, HttpSession ses) throws BadRequestException, UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        return projectionService.removeProjection(projectionId, user.getId());
    }

    private boolean validateNewProjection(AddProjectionDTO dto) throws BadRequestException {
        return
                validateStartAt(dto);
    }

    private boolean validateStartAt(AddProjectionDTO dto) throws BadRequestException {
        if (dto.getTime().isBlank()) {
            throw new BadRequestException("Please fill all requested fields");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dto.getTime(), formatter);
        if (dateTime.isAfter(LocalDateTime.now())) {
            dto.setStartAt(dateTime);
            return true;
        }
        throw new BadRequestException("Starting time must be after " + LocalDateTime.now());
    }




}