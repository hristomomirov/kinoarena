package com.finals.kinoarena.Model.DTO;

import com.finals.kinoarena.Model.Entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ResponseTicketDTO {

    private int id;
    private UserWithoutTicketAndPassDTO owner;
    private Cinema cinema;
    private Hall hall;
    private Projection projection;
    private int seat;

    public ResponseTicketDTO(Ticket t) {
        id = t.getId();
        owner = new UserWithoutTicketAndPassDTO(t.getOwner());
        cinema = t.getCinema();
        hall = t.getHall();
        projection = t.getProjection();
        seat = t.getSeat();

    }
}
