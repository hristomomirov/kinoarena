package com.finals.kinoarena.Model.DTO;


import com.finals.kinoarena.Model.Entity.Projection;
import com.finals.kinoarena.Model.Entity.Seat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class HalfProjectionDTO {


    private int id;
    private String title;
    private int length;
    private int ageRestriction;
    private LocalDateTime startAt;
    private HallWithoutCinemaDTO hall;
    private CinemaWithoutHallDTO cinema;
    private List<Seat> seats;

    public HalfProjectionDTO(Projection p) {
        id = p.getId();
        title = p.getTitle();
        length = p.getLength();
        ageRestriction = p.getAgeRestriction();
        startAt = p.getStartAt();
        hall = new HallWithoutCinemaDTO(p.getHall());
        cinema = new CinemaWithoutHallDTO(p.getHall().getCinema());
        seats = new ArrayList<>(p.getFreeSeats());
    }
}
