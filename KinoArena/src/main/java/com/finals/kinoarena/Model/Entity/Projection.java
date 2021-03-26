package com.finals.kinoarena.Model.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;


@Getter
@Setter
@Entity
@Table(name = "projections")
public class Projection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private int length;
    private String description;
    private int ageRestriction;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    @JsonManagedReference
    private Genre genre;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    @ManyToOne
    @JoinColumn(name = "hall_id")
    @JsonManagedReference
    private Hall hall;
    @Transient
    @JsonManagedReference
    private ConcurrentHashMap<Integer, HashSet<Integer>> freePlaces;

    public Projection() {
        freePlaces = new ConcurrentHashMap<>();
        for (int i = 1; i <= 5; i++) {
            freePlaces.put(i, new HashSet<>());
            for (int j = 1; j <= 20; j++) {
                freePlaces.get(i).add(j);
            }
        }
    }
}

