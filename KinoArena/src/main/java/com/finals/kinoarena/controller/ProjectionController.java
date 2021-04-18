package com.finals.kinoarena.controller;

import com.finals.kinoarena.model.DTO.GenreDTO;
import com.finals.kinoarena.util.exceptions.BadRequestException;
import com.finals.kinoarena.util.exceptions.UnauthorizedException;
import com.finals.kinoarena.model.DTO.RequestProjectionDTO;
import com.finals.kinoarena.model.DTO.ResponseProjectionDTO;
import com.finals.kinoarena.model.entity.User;
import com.finals.kinoarena.service.ProjectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RestController
public class ProjectionController extends AbstractController {

    @Autowired
    private ProjectionService projectionService;

    @GetMapping(value = "/projections/{projection_id}")
    public ResponseProjectionDTO getProjectionById(@PathVariable(name = "projection_id") int projectionId) {
        return projectionService.getProjectionById(projectionId);
    }

    @GetMapping(value = "/projections/{projection_id}/places")
    public List<Integer> getFreePlacesForProjection(@PathVariable(name = "projection_id") int projectionId) throws BadRequestException {
        return projectionService.getFreePlaces(projectionId);
    }

    @GetMapping(value = "/cinema/{cinema_id}/projections")
    public List<ResponseProjectionDTO> getAllProjectionsForCinema(@PathVariable(name = "cinema_id") int cinemaId) {
        return projectionService.getProjectionByCinema(cinemaId);
    }

    @GetMapping(value = "/city/{city}/projections")
    public List<ResponseProjectionDTO> getAllProjectionsForCity(@PathVariable String city) {
        return projectionService.getProjectionByCity(city);
    }
    @GetMapping(value = "/genre/{genre_id}/projections")
    public List<ResponseProjectionDTO> getAllProjectionsByGenre(@PathVariable int genre_id) {
        return projectionService.getAllProjectionsByGenre(genre_id);
    }

    @PostMapping(value = "/projections")
    public ResponseProjectionDTO addProjection(@Valid @RequestBody RequestProjectionDTO requestProjectionDTO, HttpSession ses) throws BadRequestException, UnauthorizedException, SQLException {
        User user = sessionManager.getLoggedUser(ses);
        if (!validateStartAt(requestProjectionDTO)) {
            throw new BadRequestException("Please fill all requested fields");
        }
        return projectionService.addProjection(requestProjectionDTO, user.getId());
    }

    @PutMapping(value = "/projections/{projection_id}")
    public ResponseProjectionDTO editProjection(@Valid @RequestBody RequestProjectionDTO requestProjectionDTO, HttpSession ses,
                                                @PathVariable(name = "projection_id") int projectionId) throws BadRequestException, UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        if (!validateStartAt(requestProjectionDTO)) {
            throw new BadRequestException("Please fill all requested fields");
        }
        return projectionService.editProjection(user.getId(), requestProjectionDTO,projectionId);
    }

    @DeleteMapping(value = "/projections/{projection_id}")
    public ResponseProjectionDTO deleteProjection(@PathVariable(name = "projection_id") int projectionId, HttpSession ses) throws UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        return projectionService.removeProjection(projectionId, user.getId());
    }

    @GetMapping(value = "/genres")
    public List<GenreDTO> getAllGenres(){
        return projectionService.getAllGenres();
    }

    private boolean validateStartAt(RequestProjectionDTO dto) throws BadRequestException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dto.getTime(), formatter);
        if (dateTime.isAfter(LocalDateTime.now())) {
            dto.setStartAt(dateTime);
            return true;
        }
        throw new BadRequestException("Starting time must be after " + LocalDateTime.now());
    }




}