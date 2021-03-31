package com.finals.kinoarena.controller;

import com.finals.kinoarena.DAO.SeatDAO;
import com.finals.kinoarena.exceptions.BadRequestException;
import com.finals.kinoarena.exceptions.UnauthorizedException;
import com.finals.kinoarena.model.DTO.ReserveTicketDTO;
import com.finals.kinoarena.model.DTO.ResponseTicketDTO;
import com.finals.kinoarena.model.DTO.StatisticsDTO;
import com.finals.kinoarena.model.entity.User;
import com.finals.kinoarena.service.TicketService;
import com.finals.kinoarena.util.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;


@Component
@RestController
public class TicketController extends AbstractController {

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private SeatDAO seatDAO;

    @GetMapping(value = "/tickets")
    public List<ResponseTicketDTO> getUserTickets(HttpSession ses) throws UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        return ticketService.getAllUserTickets(user);
    }
    @GetMapping(value = "/tickets/statistics")
    public List<StatisticsDTO> getAllSoldTickets(){
        return ticketService.getAllSoldTickets();
    }

    @PutMapping(value = "/projection/{projection_id}/ticket")
    public ResponseTicketDTO reserveTicket(@RequestBody ReserveTicketDTO reserveTicketDTO, HttpSession ses,
                                           @PathVariable(name = "projection_id") int projectionId)
                                           throws UnauthorizedException, BadRequestException, SQLException {
        User user = sessionManager.getLoggedUser(ses);
        if (!validateReservation(reserveTicketDTO)) {
            throw new BadRequestException("Invalid seat number");
        }
        return ticketService.reserveTicket(projectionId, user, reserveTicketDTO);
    }


    private boolean validateReservation(ReserveTicketDTO reserveTicketDTO) throws SQLException {
        return validateSeat(reserveTicketDTO.getSeat());
    }

    private boolean validateSeat(int seat) throws SQLException {

        return seat >= 1 && seat <=seatDAO.getMaxSeatNum();
    }
}
