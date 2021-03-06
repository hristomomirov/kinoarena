package com.finals.kinoarena.model.DTO;

import com.finals.kinoarena.model.entity.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenreDTO {
    private int id;
    private String type;

    public GenreDTO(Genre genre){
        id = genre.getId();
        type = genre.getType();
    }
}