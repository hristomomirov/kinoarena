package com.finals.kinoarena.model.DTO;

import com.finals.kinoarena.model.entity.Ticket;
import com.finals.kinoarena.model.entity.User;
import com.finals.kinoarena.model.entity.UserStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserWithoutPassDTO {

    private int id;
    private String username;
    private String email;
    private String name;
    private int age;
    private String status;
    private List<ResponseTicketDTO> tickets;

    public UserWithoutPassDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getFirstName() + " " + user.getLastName();
        this.age = user.getAge();
        this.status = UserStatus.values()[user.getStatusId() - 1].toString().toLowerCase();
        this.tickets = new ArrayList<>();
        for (Ticket ticket : user.getTickets()) {
            this.tickets.add(new ResponseTicketDTO(ticket));
        }

    }
}
