package com.finals.kinoarena.Model.DTO;


import com.finals.kinoarena.Model.Entity.Projection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    public HalfProjectionDTO(Projection p) {
        id = p.getId();
        title = p.getTitle();
        length = p.getLength();
        ageRestriction = p.getAgeRestriction();
        startAt = p.getStartAt();
        hall = new HallWithoutCinemaDTO(p.getHall());
        cinema = new CinemaWithoutHallDTO(p.getHall().getCinema());
    }
}
