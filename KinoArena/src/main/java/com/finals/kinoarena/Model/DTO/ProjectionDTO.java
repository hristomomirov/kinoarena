package com.finals.kinoarena.Model.DTO;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.finals.kinoarena.Model.Entity.Genre;
import com.finals.kinoarena.Model.Entity.Hall;
import com.finals.kinoarena.Model.Entity.Projection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class ProjectionDTO {

    private int id;
    private String title;
    private int length;
    private String description;
    private int ageRestriction;
    private Genre genre;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private HallDTO hall;

    ProjectionDTO(Projection projection){
        id = projection.getId();
        title = projection.getTitle();
        length=projection.getLength();
        description=projection.getDescription();
        ageRestriction=projection.getAgeRestriction();
        genre=projection.getGenre();
        startAt = projection.getStartAt();
        endAt = projection.getEndAt();
        hall = new HallDTO(projection.getHall());
    }
}
