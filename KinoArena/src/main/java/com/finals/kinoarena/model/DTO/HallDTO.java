package com.finals.kinoarena.model.DTO;

import com.finals.kinoarena.model.entity.Hall;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HallDTO {

    private int id;
    private int number;
    private int capacity;
    private int cinemaId;
    private CinemaWithoutHallDTO cinema;
//TODO add request and response DTO
    public HallDTO(Hall hall) {
        this.id = hall.getId();
        this.number = hall.getNumber();
        this.capacity = hall.getCapacity();
        this.cinema = new CinemaWithoutHallDTO(hall.getCinema());
    }
}
