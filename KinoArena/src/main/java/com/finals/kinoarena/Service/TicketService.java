package com.finals.kinoarena.Service;

import com.finals.kinoarena.DAO.ProjectionDAO;
import com.finals.kinoarena.DAO.SeatDAO;
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
    private ProjectionDAO projectionDAO;
    @Autowired
    private SeatDAO seatDAO;

    public List<TicketWithoutUserDTO> getAllUserTickets(User user) {
        return ticketRepository.findAllByOwnerId(user.getId());
    }

    public ResponseTicketDTO reserveTicket(int cinemaId, int projectionId, User user, ReserveTicketDTO reserveTicketDTO) throws BadRequestException, SQLException {
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
        if (seatsAreTaken(projectionId, reserveTicketDTO.getSeat())) {
            throw new BadRequestException("Seats already taken.Please select different seats");
        }
        Projection projection = sProjection.get();
        Ticket ticket = new Ticket();
        ticket.setOwner(user);
        ticket.setProjection(projection);
        ticket.setSeat(reserveTicketDTO.getSeat());
        seatDAO.removeSeat(reserveTicketDTO.getSeat(),projectionId);
        ticket.setPurchasedAt(LocalDateTime.now());
        return new ResponseTicketDTO(ticketRepository.save(ticket));
    }

    private boolean seatsAreTaken(int projectionId, int seat) throws SQLException, BadRequestException {
        List<Integer> freeSeats = seatDAO.getFreeSeatsForProjection(projectionId);
        if (freeSeats.isEmpty()){
            throw new BadRequestException("We are sorry, all seats are taken");
        }
       return !freeSeats.contains(seat);
    }

    private boolean cinemaHasProjection(int cinemaId, int projectionId) throws SQLException {
        return projectionDAO.isProjectionInCinema(cinemaId, projectionId);
    }

}
