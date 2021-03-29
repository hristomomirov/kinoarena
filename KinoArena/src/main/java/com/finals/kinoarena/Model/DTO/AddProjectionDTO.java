package com.finals.kinoarena.Model.DTO;

import com.finals.kinoarena.Model.Entity.Hall;
import com.finals.kinoarena.Model.Entity.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
public class AddProjectionDTO {

    private int movieId;
    private int hallId;
    private String time;
    private Movie movie;
    private Hall hall;
    private LocalDateTime startAt;

}
