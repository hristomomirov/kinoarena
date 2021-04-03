package com.finals.kinoarena.model.DTO;

import com.finals.kinoarena.model.entity.Cinema;
import com.finals.kinoarena.model.entity.Hall;
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
public class ResponseCinemaDTO {

    private int id;
    private String name;
    private String city;
    private List<HallWithoutCinemaDTO> halls;

    public ResponseCinemaDTO(Cinema c){
        this.id = c.getId();
        this.name = c.getName();
        this.city = c.getCity();
        halls = new ArrayList<>();
        for (Hall h: c.getHalls()) {
            halls.add(new HallWithoutCinemaDTO(h));
        }
    }
}
