package com.finals.kinoarena.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
public class AddProjectionDTO {

    private int movieId;
    private Integer hallId;
    private String time;
    private LocalDateTime startAt;

}
