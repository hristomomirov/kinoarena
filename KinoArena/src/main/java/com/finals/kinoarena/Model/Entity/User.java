package com.finals.kinoarena.Model.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.finals.kinoarena.Model.DTO.RegisterDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Component
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    public static final int ROLE_USER = 1;
    public static final int ROLE_ADMIN = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private int age;
    private int roleId;
    private int statusId;
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "owner")
    @JsonManagedReference
    private List<Ticket> tickets;

    public User(RegisterDTO dto) {
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.email = dto.getEmail();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.age = dto.getAge();
        this.roleId = ROLE_USER;
        this.statusId = UserStatus.valueOf(dto.getStatus().toUpperCase()).ordinal() + 1;
        this.createdAt = LocalDateTime.now();
        this.tickets = new ArrayList<>();

    }
}

