package com.finals.kinoarena.Service;

import com.finals.kinoarena.DAO.TicketDAO;
import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Exceptions.NotFoundException;
import com.finals.kinoarena.Model.DTO.ReserveTicketDTO;
import com.finals.kinoarena.Model.DTO.ResponseTicketDTO;
import com.finals.kinoarena.Model.DTO.TicketWithoutUserDTO;
import com.finals.kinoarena.Model.Entity.*;
import com.finals.kinoarena.Model.Repository.CinemaRepository;
import com.finals.kinoarena.Model.Repository.ProjectionRepository;
import com.finals.kinoarena.Model.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private TicketDAO ticketDAO;

    public List<TicketWithoutUserDTO> getAllUserTickets(User user) {
        return ticketRepository.findAllByOwnerId(user.getId());
    }

    public ResponseTicketDTO reserveTicket(int cinemaId, int projectionId, User user, ReserveTicketDTO reserveTicketDTO) throws BadRequestException, SQLException {
        Hall hall = projectionRepository.findById(projectionId).get().getHall();
        Optional<Cinema> sCinema = cinemaRepository.findById(cinemaId);
        Optional<Projection> sProjection = projectionRepository.findById(projectionId);
        if (sCinema.isEmpty()) {
            throw new NotFoundException("Cinema does not exist");
        }
        if (sProjection.isEmpty()) {
            throw new NotFoundException("Projection does not exist");
        }
        if (!cinemaHasProjection(cinemaId, projectionId)) {
            throw new BadRequestException("There is no such projection at that cinema");
        }
        if (seatsAreTaken(projectionId, reserveTicketDTO)) {
            throw new BadRequestException("Seats already taken.Please select different seats");
        }
        Cinema cinema = sCinema.get();
        Projection projection = sProjection.get();

        Ticket ticket = new Ticket();
        ticket.setOwner(user);
        ticket.setCinema(cinema);
        ticket.setHall(hall);
        ticket.setProjection(projection);
        ticket.setSeat(reserveTicketDTO.getSeat());
        ticket.setRow(reserveTicketDTO.getRow());
        ticket.setPurchasedAt(LocalDateTime.now());
        return new ResponseTicketDTO(ticketRepository.save(ticket));
    }

    private boolean seatsAreTaken(int projectionId, ReserveTicketDTO reserveTicketDTO) {
        List<Ticket> tickets = ticketRepository.findAllByProjectionId(projectionId);
        for (Ticket t : tickets) {
            if (t.getSeat() == reserveTicketDTO.getSeat() &&
                    t.getRow() == reserveTicketDTO.getRow()) {
                return true;
            }
        }
        return false;
    }

    private boolean cinemaHasProjection(int cinemaId, int projectionId) throws SQLException {
        return ticketDAO.getProjectionsInCinema(cinemaId, projectionId);
    }

}
