package com.finals.kinoarena.Model.DTO;

import com.finals.kinoarena.Model.Entity.Cinema;
import com.finals.kinoarena.Model.Entity.Hall;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
