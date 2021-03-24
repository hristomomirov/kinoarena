package com.finals.kinoarena.Model.DTO;

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
    private Integer age;
    private String status;
}
