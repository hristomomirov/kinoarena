package com.finals.kinoarena.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EditUserPasswordDTO {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
