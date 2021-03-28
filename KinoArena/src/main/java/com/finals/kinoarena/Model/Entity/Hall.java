package com.finals.kinoarena.Model.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.finals.kinoarena.Model.DTO.HallDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
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
    @JsonBackReference(value = "hall-projection") //TODO
    private List<Projection> projections;

    public Hall(HallDTO hallDTO) {
        number = hallDTO.getNumber();
        capacity=hallDTO.getCapacity();
    }
}
