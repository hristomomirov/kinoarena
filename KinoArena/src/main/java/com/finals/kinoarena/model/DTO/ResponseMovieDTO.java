package com.finals.kinoarena.model.DTO;

import com.finals.kinoarena.model.entity.Genre;
import com.finals.kinoarena.model.entity.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseMovieDTO {

    private int id;
    private String title;
    private String description;
    private int length;
    private int ageRestriction;
    private Genre genre;

    public ResponseMovieDTO(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.length = movie.getLength();
        this.ageRestriction = movie.getAgeRestriction();
        this.genre = movie.getGenre();
    }
}
