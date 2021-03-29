package com.finals.kinoarena.Model.DTO;


import com.finals.kinoarena.Model.Entity.Movie;
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
    private Movie movie;
    private LocalDateTime startAt;
    private HallWithoutCinemaDTO hall;
    private CinemaWithoutHallDTO cinema;

    public HalfProjectionDTO(Projection p) {
        id = p.getId();
        startAt = p.getStartAt();
        movie = p.getMovie();
        hall = new HallWithoutCinemaDTO(p.getHall());
        cinema = new CinemaWithoutHallDTO(p.getHall().getCinema());
    }
}
