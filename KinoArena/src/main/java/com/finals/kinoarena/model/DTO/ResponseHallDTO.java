package com.finals.kinoarena.model.DTO;


import com.finals.kinoarena.model.entity.Hall;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseHallDTO {

    private int id;
    private int number;
    private int capacity;
    private CinemaWithoutHallDTO cinema;

    public ResponseHallDTO(Hall hall) {
        this.id = hall.getId();
        this.number = hall.getNumber();
        this.capacity = hall.getCapacity();
        this.cinema = new CinemaWithoutHallDTO(hall.getCinema());
    }
}