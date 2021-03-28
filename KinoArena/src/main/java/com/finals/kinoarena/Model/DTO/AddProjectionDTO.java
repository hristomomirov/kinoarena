package com.finals.kinoarena.Model.DTO;


import com.finals.kinoarena.Model.Entity.Genre;
import com.finals.kinoarena.Model.Entity.Hall;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
public class AddProjectionDTO {

    private String title;
    private int length;
    private String description;
    private int ageRestriction;
    private String type;
    private String time;
    private int hallId;
    private Hall hall;
    private Genre genre;
    private LocalDateTime startAt;

}
