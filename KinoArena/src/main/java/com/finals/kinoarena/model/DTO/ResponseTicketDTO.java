package com.finals.kinoarena.model.DTO;

import com.finals.kinoarena.model.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseTicketDTO {

    private int id;
    private UserWithoutTicketAndPassDTO owner;
    private ResponseProjectionDTO projection;
    private int seat;

    public ResponseTicketDTO(Ticket ticket) {
        id = ticket.getId();
        owner = new UserWithoutTicketAndPassDTO(ticket.getOwner());
        projection = new ResponseProjectionDTO(ticket.getProjection());
        seat = ticket.getSeat();

    }
}
