package com.finals.kinoarena.controller;

import com.finals.kinoarena.DAO.SeatDAO;
import com.finals.kinoarena.util.exceptions.BadRequestException;
import com.finals.kinoarena.util.exceptions.UnauthorizedException;
import com.finals.kinoarena.model.DTO.ReserveTicketDTO;
import com.finals.kinoarena.model.DTO.ResponseTicketDTO;
import com.finals.kinoarena.model.DTO.StatisticsDTO;
import com.finals.kinoarena.model.entity.User;
import com.finals.kinoarena.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;


@Component
@RestController
public class TicketController extends AbstractController {

    @Autowired
    private TicketService ticketService;


    @GetMapping(value = "/tickets")
    public List<ResponseTicketDTO> getUserTickets(HttpSession ses) throws UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        return ticketService.getAllUserTickets(user);
    }

    @GetMapping(value = "/tickets/statistics")
    public List<StatisticsDTO> getAllSoldTickets(HttpSession ses) throws UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        return ticketService.getAllSoldTickets(user);
    }

    @PostMapping(value = "/projection/{projection_id}/ticket")
    public ResponseTicketDTO reserveTicket(@Valid @RequestBody ReserveTicketDTO reserveTicketDTO, HttpSession ses,
                                           @PathVariable(name = "projection_id") int projectionId)
            throws UnauthorizedException, BadRequestException, SQLException {
        User user = sessionManager.getLoggedUser(ses);
        return ticketService.reserveTicket(projectionId, user, reserveTicketDTO);
    }
}

