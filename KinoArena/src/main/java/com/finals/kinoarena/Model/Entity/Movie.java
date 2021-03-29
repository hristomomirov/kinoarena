package com.finals.kinoarena.Model.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.finals.kinoarena.Model.DTO.AddMovieDTO;
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

    public Movie(AddMovieDTO addMovieDTO) {
        this.title = addMovieDTO.getTitle();
        this.description = addMovieDTO.getDescription();
        this.length = addMovieDTO.getLength();
        this.ageRestriction = addMovieDTO.getAgeRestriction();
        this.genre = addMovieDTO.getGenre();
    }
}
