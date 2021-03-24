package com.finals.kinoarena.Model.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private int age;
    @JsonIgnore
    private long roleId;
    @JsonIgnore
    private long statusId;
    @JsonIgnore
    private LocalDateTime createdAt;

}
