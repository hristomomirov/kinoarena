package com.finals.kinoarena.model.DTO;

import com.finals.kinoarena.model.entity.User;
import com.finals.kinoarena.model.entity.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserWithoutTicketAndPassDTO {

    private int id;
    private String username;
    private String email;
    private String name;
    private int age;
    private String status;

    public UserWithoutTicketAndPassDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getFirstName() + " " + user.getLastName();
        this.age = user.getAge();
        this.status = UserStatus.values()[user.getStatusId() - 1].toString().toLowerCase();
    }
}
