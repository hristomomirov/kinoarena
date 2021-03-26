package com.finals.kinoarena.Model.DTO;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.finals.kinoarena.Model.Entity.Genre;
import com.finals.kinoarena.Model.Entity.Hall;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private Hall hall;
    private Genre genre;
    private LocalDateTime startAt;

}
