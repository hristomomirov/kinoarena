package Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Projection {

    @Id
    private long id;
    private String title;
    private int length;
    private String description;
    private int ageRestriction;
    private long genreId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;


}
