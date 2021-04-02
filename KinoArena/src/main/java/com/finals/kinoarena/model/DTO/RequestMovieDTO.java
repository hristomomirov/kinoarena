package com.finals.kinoarena.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestMovieDTO {

    private String imdbId;
    private int length;
    private int ageRestriction;
    private Integer genreId;
}
