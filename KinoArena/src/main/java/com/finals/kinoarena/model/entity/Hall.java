package com.finals.kinoarena.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.finals.kinoarena.model.DTO.RequestHallDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "halls")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int number;
    private int capacity;
    @ManyToOne
    @JoinColumn(name = "cinema_id")
    @JsonBackReference
    private Cinema cinema;
    @OneToMany(mappedBy = "hall")
    @JsonManagedReference(value = "hall-projection")
    private List<Projection> projections;

    public Hall(RequestHallDTO requestHallDTO) {
        number = requestHallDTO.getNumber();
        capacity= requestHallDTO.getCapacity();
    }
}
