package com.finals.kinoarena.Model.DTO;

import com.finals.kinoarena.Model.Entity.Cinema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class CinemaDTO {

    private int id;
    private String name;
    private String city;

    public CinemaDTO(Cinema c){
        this.id = c.getId();
        this.name = c.getName();
        this.city = c.getCity();
    }

    @Override
    public String toString() {
        return "CinemaDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
