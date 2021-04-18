package com.finals.kinoarena.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class RequestCinemaDTO {

    @NotBlank(message = "Please fill all necessary fields")
    @NotNull(message = "Please fill all necessary fields")
    @Pattern(regexp = "^(\\w+\\s?)*$",message = "Name must contain only letters and numbers")
    private String name;
    @NotBlank(message = "Please fill all necessary fields")
    @NotNull(message = "Please fill all necessary fields")
    @Pattern(regexp = "^([a-zA-Z]+\\s?)*$",message = "City must contain only letters")
    private String city;
}