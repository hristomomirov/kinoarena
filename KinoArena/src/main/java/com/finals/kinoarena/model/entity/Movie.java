package com.finals.kinoarena.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.finals.kinoarena.model.DTO.addMovieDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String year;
    private String plot;
    private int length;
    private double rating;
    private int ageRestriction;
    private String leadingActor;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    @JsonBackReference
    private Genre genre;
    private String poster;
    private String imdbId;
    @OneToMany(mappedBy = "movie")
    @JsonManagedReference
    private List<Projection> projections;
}
