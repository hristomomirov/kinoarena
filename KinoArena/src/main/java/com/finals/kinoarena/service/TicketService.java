package com.finals.kinoarena.service;

import com.finals.kinoarena.util.exceptions.BadRequestException;
import com.finals.kinoarena.util.exceptions.NotFoundException;
import com.finals.kinoarena.model.DTO.ReserveTicketDTO;
import com.finals.kinoarena.model.DTO.ResponseTicketDTO;
import com.finals.kinoarena.model.DTO.StatisticsDTO;
import com.finals.kinoarena.model.entity.Projection;
import com.finals.kinoarena.model.entity.Ticket;
import com.finals.kinoarena.model.entity.User;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService extends AbstractService {

    public List<ResponseTicketDTO> getAllUserTickets(User user) {
        return ticketRepository.findAllByOwnerId(user.getId());
    }

    public ResponseTicketDTO reserveTicket(int projectionId, User user, ReserveTicketDTO reserveTicketDTO) throws BadRequestException, SQLException {
        Optional<Projection> sProjection = projectionRepository.findById(projectionId);
        if (sProjection.isEmpty()) {
            throw new NotFoundException("Projection does not exist");
        }
        Projection projection = sProjection.get();
        if (seatsAreTaken(projectionId, reserveTicketDTO.getSeat())) {
            throw new BadRequestException("Seats already taken.Please select different seats");
        }
        Ticket ticket = new Ticket();
        ticket.setOwner(user);
        ticket.setProjection(projection);
        ticket.setSeat(reserveTicketDTO.getSeat());
        seatDAO.removeSeat(reserveTicketDTO.getSeat(), projectionId);
        ticket.setPurchasedAt(LocalDateTime.now());
        return new ResponseTicketDTO(ticketRepository.save(ticket));
    }

    private boolean seatsAreTaken(int projectionId, int seat) throws BadRequestException {
        List<Integer> freeSeats = seatDAO.getFreeSeatsForProjection(projectionId);
        if (freeSeats.isEmpty()) {
            throw new BadRequestException("We are sorry, all seats are taken");
        }
        return !freeSeats.contains(seat);
    }
    public List<StatisticsDTO> getAllSoldTickets(){
        return statisticsDAO.soldTicketsPerProjection();
    }
}
