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
    private String year;
    private String plot;
    private int length;
    private double rating;
    private int ageRestriction;
    private String lead;
    private Genre genre;
    private String poster;

    public ResponseMovieDTO(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.year = movie.getYear();
        this.plot = movie.getPlot();
        this.length = movie.getLength();
        this.rating = movie.getRating();
        this.ageRestriction = movie.getAgeRestriction();
        this.lead = movie.getLeadingActor();
        this.genre = movie.getGenre();
        this.poster = movie.getPoster();
    }
}
