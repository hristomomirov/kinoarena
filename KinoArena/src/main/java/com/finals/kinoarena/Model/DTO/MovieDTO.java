package com.finals.kinoarena.Model.DTO;

import com.finals.kinoarena.Model.Entity.Genre;
import com.finals.kinoarena.Model.Entity.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovieDTO {

    private int id;
    private String title;
    private String description;
    private int length;
    private int ageRestriction;
    private Genre genre;

    public MovieDTO(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.length = movie.getLength();
        this.ageRestriction = movie.getAgeRestriction();
        this.genre = movie.getGenre();
    }
}
