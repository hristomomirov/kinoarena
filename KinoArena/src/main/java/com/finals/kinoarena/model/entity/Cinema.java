package com.finals.kinoarena.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "cinemas")
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String city;
    @OneToMany(mappedBy = "cinema")
    @JsonManagedReference(value = "cinema-hall")
    List<Hall> halls;

    public Cinema(String name, String city) {
        this.name = name;
        this.city = city;
        this.halls = new ArrayList<>();
    }
}
