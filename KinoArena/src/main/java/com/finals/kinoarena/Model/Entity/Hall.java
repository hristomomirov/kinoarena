package com.finals.kinoarena.Model.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "halls")
public class Hall {

    @Id
    private int id;
    private int number;
    private int capacity;
    private int cinemaId;
}
