package com.finals.kinoarena.model.DTO;


import com.finals.kinoarena.model.entity.Projection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ResponseProjectionDTO {


    private int id;
    private ResponseMovieDTO movie;
    private LocalDateTime startAt;
    private HallWithoutCinemaDTO hall;
    private CinemaWithoutHallDTO cinema;

    public ResponseProjectionDTO(Projection p) {
        id = p.getId();
        startAt = p.getStartAt();
        movie = new ResponseMovieDTO(p.getMovie());
        hall = new HallWithoutCinemaDTO(p.getHall());
        cinema = new CinemaWithoutHallDTO(p.getHall().getCinema());
    }
}
