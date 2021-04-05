package com.finals.kinoarena.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.finals.kinoarena.model.DTO.AddProjectionDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "projections")
public class Projection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonBackReference
    private Movie movie;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    @ManyToOne
    @JoinColumn(name = "hall_id")
    @JsonBackReference(value = "hall-projection")
    private Hall hall;
    @ManyToMany
    @JoinTable(
            name = "projections_have_seats",
            joinColumns = {@JoinColumn(name = "projection_id")},
            inverseJoinColumns = {@JoinColumn(name = "seat_id")}
    )
    @JsonManagedReference
    List<Seat> reservedSeats;

    public Projection(AddProjectionDTO dto) {
        startAt = dto.getStartAt();
        reservedSeats = new ArrayList<>();
    }
}

