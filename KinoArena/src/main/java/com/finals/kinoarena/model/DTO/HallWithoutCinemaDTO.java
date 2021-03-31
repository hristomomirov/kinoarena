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
public class HallWithoutCinemaDTO {

    private int id;
    private int number;
    private int capacity;

    public HallWithoutCinemaDTO(Hall hall) {
        this.id = hall.getId();
        this.number = hall.getNumber();
        this.capacity = hall.getCapacity();
    }
}
