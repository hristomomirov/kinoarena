package Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
//@Table(name = "users")
public class User {

    @Id
    private long id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private int age;
    private long roleId;
    private long statusId;
    private LocalDateTime createdAt;
}
