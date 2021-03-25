package com.finals.kinoarena.Model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserPasswordDTO {

    private int id;
    private String oldPassword;
    private String password;
    private String confirmPassword;
}
