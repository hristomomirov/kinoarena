package com.finals.kinoarena.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finals.kinoarena.Model.DTO.RequestRegisterUserDTO;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private int age;
    @JsonIgnore
    private int roleId;
    @JsonIgnore
    private int statusId;
    @JsonIgnore
    private LocalDateTime createdAt;


    public User(RequestRegisterUserDTO userDTO) {
    }
}

