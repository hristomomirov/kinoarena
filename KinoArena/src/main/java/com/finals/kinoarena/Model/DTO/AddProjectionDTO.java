package com.finals.kinoarena.Model.DTO;

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
    private LocalDateTime startAt;

}
