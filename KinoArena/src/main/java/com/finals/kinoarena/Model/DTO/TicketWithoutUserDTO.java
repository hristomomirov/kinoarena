package com.finals.kinoarena.Model.DTO;

import com.finals.kinoarena.Model.Entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class TicketWithoutUserDTO {

    private int id;
    private Cinema cinema;
    private Hall hall;
    private HalfProjectionDTO projection;
    private int seat;

    public TicketWithoutUserDTO(Ticket t) {
        id = t.getId();
        cinema = t.getCinema();
        hall = t.getHall();
        projection = new HalfProjectionDTO(t.getProjection());
        seat = t.getSeat();
    }
}
