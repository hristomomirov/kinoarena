package com.finals.kinoarena.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.finals.kinoarena.model.DTO.RequestMovieDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private int length;
    private int ageRestriction;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    @JsonBackReference
    private Genre genre;
    @OneToMany(mappedBy = "movie")
    @JsonManagedReference
    private List<Projection> projections;
    private String imdbId;

    public Movie(RequestMovieDTO requestMovieDTO) {
        this.title = requestMovieDTO.getTitle();
        this.description = requestMovieDTO.getDescription();
        this.length = requestMovieDTO.getLength();
        this.ageRestriction = requestMovieDTO.getAgeRestriction();
        this.imdbId = requestMovieDTO.getImdbId();
    }
}
