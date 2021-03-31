package com.finals.kinoarena.model.DTO;

import com.finals.kinoarena.model.entity.Projection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
public class ProjectionDTO {

    private int id;
    private String title;
    private int length;
    private String description;
    private int ageRestriction;
    private String genre;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private HallDTO hall;

    public ProjectionDTO(Projection projection){
        id = projection.getId();
        startAt = projection.getStartAt();
        endAt = projection.getEndAt();
        hall = new HallDTO(projection.getHall());
        title =projection.getMovie().getTitle();
        description = projection.getMovie().getDescription();
        genre = projection.getMovie().getGenre().getType();
    }
}
