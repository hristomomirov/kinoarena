package com.finals.kinoarena.Model.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.finals.kinoarena.Model.DTO.AddProjectionDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
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
            joinColumns = { @JoinColumn(name = "projection_id") },
            inverseJoinColumns = { @JoinColumn(name = "seat_id") }
    )
    List<Seat> freeSeats;

    public Projection(AddProjectionDTO dto) {
        movie = dto.getMovie();
        startAt = dto.getStartAt();
        endAt = startAt.plusMinutes(movie.getLength());
        hall = dto.getHall();
    }
}


