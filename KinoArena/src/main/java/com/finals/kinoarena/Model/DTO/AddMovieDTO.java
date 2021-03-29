package com.finals.kinoarena.Model.DTO;

import com.finals.kinoarena.Model.Entity.Genre;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddMovieDTO {


    private String title;
    private String description;
    private int length;
    private int ageRestriction;
    private Integer genreId;
    private Genre genre;
    private String imdbId;
}
