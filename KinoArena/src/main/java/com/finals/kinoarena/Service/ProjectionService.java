package com.finals.kinoarena.Service;

import com.finals.kinoarena.DAO.ProjectionDAO;
import com.finals.kinoarena.DAO.TicketDAO;
import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Exceptions.NotFoundException;
import com.finals.kinoarena.Model.DTO.ProjectionDTO;
import com.finals.kinoarena.Model.Entity.Projection;
import com.finals.kinoarena.Model.Repository.ProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class ProjectionService extends AbstractService {

    @Autowired
    private ProjectionRepository projectionRepository;

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

}
