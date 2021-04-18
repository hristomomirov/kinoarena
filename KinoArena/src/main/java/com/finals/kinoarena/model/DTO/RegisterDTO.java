package com.finals.kinoarena.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {

    @NotBlank(message = "Please fill all necessary fields")
    @NotNull(message = "Please fill all necessary fields")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username can include only letters and numbers")
    private String username;

    @NotBlank(message = "Please fill all necessary fields")
    @NotNull(message = "Please fill all necessary fields")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$",message = "Password must be between 8 and 20 symbols and must contain at least one upper and lower case letters and a number")
    private String password;

    @NotNull(message = "Please fill all necessary fields")
    private String confirmPassword;

    @Email(message = "You must enter a valid email")
    @NotBlank(message = "Please fill all necessary fields")
    @NotNull(message = "Please fill all necessary fields")
    private String email;

    @NotBlank(message = "Please fill all necessary fields")
    @NotNull(message = "Please fill all necessary fields")
    @Pattern(regexp = "^[A-Z][-a-zA-Z]+$",message = "Name must contain only letters")
    private String firstName;

    @NotBlank(message = "Please fill all necessary fields")
    @NotNull(message = "Please fill all necessary fields")
    @Pattern(regexp = "^[A-Z][-a-zA-Z]+$",message = "Name must contain only letters")
    private String lastName;

    @Min(value = 1,message = "Age cannot be less than 1")
    @Max(value = 100,message = "Age cannot be more than 100")
    @NotNull(message = "Please fill all necessary fields")
    private Integer age;

    @NotBlank(message = "Please fill all necessary fields")
    @NotNull(message = "Please fill all necessary fields")
    private String status;

}
