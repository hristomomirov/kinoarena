package com.finals.kinoarena.Model.DTO;

import com.finals.kinoarena.Model.Entity.Ticket;
import com.finals.kinoarena.Model.Entity.User;
import com.finals.kinoarena.Model.Entity.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class UserWithoutPassDTO {

    private int id;
    private String username;
    private String email;
    private String name;
    private int age;
    private String status;
    private List<Ticket> tickets;

    public UserWithoutPassDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getFirstName() + " " + user.getLastName();
        this.age = user.getAge();
        this.status = UserStatus.values()[user.getStatusId() - 1].toString().toLowerCase();
        this.tickets = new ArrayList<>();
        tickets.addAll(user.getTickets());

    }
}
