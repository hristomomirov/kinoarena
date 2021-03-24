package com.finals.kinoarena.Model.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "projections")
public class Projection {

    @Id
    private int id;
    private String title;
    private int length;
    private String description;
    private int ageRestriction;
    private int genreId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;


}
