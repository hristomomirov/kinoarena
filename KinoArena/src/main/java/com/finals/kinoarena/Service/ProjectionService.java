package com.finals.kinoarena.Service;

import com.finals.kinoarena.DAO.ProjectionDAO;
import com.finals.kinoarena.DAO.TicketDAO;
import com.finals.kinoarena.Exceptions.NotFoundException;
import com.finals.kinoarena.Model.DTO.ProjectionDTO;
import com.finals.kinoarena.Model.Entity.Projection;
import com.finals.kinoarena.Model.Repository.ProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Optional;

@Component
public class ProjectionService extends AbstractService {

    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private ProjectionDAO projectionDAO;

    public ProjectionDTO getProjectionById(int id) throws SQLException {
        Optional<Projection> sProjection = projectionRepository.findById(id);
        if (sProjection.isEmpty()) {
            throw new NotFoundException("Projection does not exist");
        }
        return projectionDAO.getProjectionsById(id);
    }
}
