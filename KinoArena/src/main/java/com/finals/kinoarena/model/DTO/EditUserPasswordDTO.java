package com.finals.kinoarena.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
@Setter
public class EditUserPasswordDTO {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
