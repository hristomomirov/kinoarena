package com.finals.kinoarena.Model.DTO;

import com.finals.kinoarena.Model.Entity.Cinema;
import com.finals.kinoarena.Model.Entity.Hall;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class CinemaDTO {

    private int id;
    private String name;
    private String city;
    private List<HallWithoutCinemaDTO> halls;

    public CinemaDTO(Cinema c){
        this.id = c.getId();
        this.name = c.getName();
        this.city = c.getCity();
        halls = new ArrayList<>();
        for (Hall h: c.getHalls()) {
            halls.add(new HallWithoutCinemaDTO(h));
        }
    }
}
