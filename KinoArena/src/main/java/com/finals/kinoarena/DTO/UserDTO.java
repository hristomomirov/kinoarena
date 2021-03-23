package com.finals.kinoarena.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finals.kinoarena.Model.User;

import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private long id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private int age;
    private long roleId;
    private long statusId;
}
