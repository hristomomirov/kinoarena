package com.finals.kinoarena.model.DTO;

import com.finals.kinoarena.model.entity.Cinema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class CinemaWithoutHallDTO {

    private int id;
    private String name;
    private String city;


    public CinemaWithoutHallDTO(Cinema c) {
        this.id = c.getId();
        this.name = c.getName();
        this.city = c.getCity();
    }
}