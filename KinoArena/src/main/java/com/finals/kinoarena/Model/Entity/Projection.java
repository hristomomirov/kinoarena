package com.finals.kinoarena.Model.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private String title;
    private int length;
    private String description;
    private int ageRestriction;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    @ManyToOne
    @JoinColumn(name = "hall_id")
    @JsonBackReference(value = "hall-projection")
    private Hall hall;
//    @ManyToMany
//    @JoinTable(
//            name = "projections_have_seats",
//            joinColumns = { @JoinColumn(name = "projection_id") },
//            inverseJoinColumns = { @JoinColumn(name = "seat_id") }
//    )
//    @JsonManagedReference
//    List<Seat> seats;

    public Projection(AddProjectionDTO dto) {
        title = dto.getTitle();
        length = dto.getLength();
        description = dto.getDescription();
        ageRestriction = dto.getAgeRestriction();
        genre = dto.getGenre();
        startAt = dto.getStartAt();
        endAt = startAt.plusMinutes(length);
        hall = dto.getHall();
    }
}


