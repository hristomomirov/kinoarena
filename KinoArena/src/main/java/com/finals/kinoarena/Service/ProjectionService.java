package com.finals.kinoarena.Service;

import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Exceptions.NotFoundException;
import com.finals.kinoarena.Model.DTO.AddProjectionDTO;
import com.finals.kinoarena.Model.DTO.ProjectionDTO;
import com.finals.kinoarena.Model.Entity.Hall;
import com.finals.kinoarena.Model.Entity.Projection;
import com.finals.kinoarena.Model.Repository.HallRepository;
import com.finals.kinoarena.Model.Repository.ProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class ProjectionService extends AbstractService {

    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private HallRepository hallRepository;

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
