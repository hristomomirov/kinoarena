package com.finals.kinoarena.Controller;

import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Exceptions.UnauthorizedException;
import com.finals.kinoarena.Model.DTO.ReserveTicketDTO;
import com.finals.kinoarena.Model.DTO.ResponseTicketDTO;
import com.finals.kinoarena.Model.DTO.TicketWithoutUserDTO;
import com.finals.kinoarena.Model.Entity.User;
import com.finals.kinoarena.Service.TicketService;
import com.finals.kinoarena.Service.UserService;
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
    private UserService userService;
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private TicketService ticketService;

    @GetMapping(value = "/tickets")
    public List<TicketWithoutUserDTO> getUserTickets(HttpSession ses) throws UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        return ticketService.getAllUserTickets(user);
    }

    @PutMapping(value = "/cinema/{cinema_id}/projection/{projection_id}")
    //TODO can accept multiple tickets at a time
    public ResponseTicketDTO reserveTicket(@RequestBody ReserveTicketDTO reserveTicketDTO, HttpSession ses,
                                           @PathVariable(name = "cinema_id") int cinemaId,
                                           @PathVariable(name = "projection_id") int projectionId)
                                           throws UnauthorizedException, BadRequestException, SQLException {
        if (!validateReservation(reserveTicketDTO)) {
            throw new BadRequestException("Seat must be between 1 and 20 and row between 1 and 5");
        }
        User user = sessionManager.getLoggedUser(ses);
        return ticketService.reserveTicket(cinemaId, projectionId, user, reserveTicketDTO);
    }

    private boolean validateReservation(ReserveTicketDTO reserveTicketDTO) {
        return validateSeat(reserveTicketDTO.getSeat()) && validateRow(reserveTicketDTO.getRow());
    }

    private boolean validateRow(int row) {
        return row >= 1 && row <= 5;
    }

    private boolean validateSeat(int seat) {
        return seat >= 1 && seat <= 20;
    }
}
