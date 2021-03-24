package com.finals.kinoarena.Model.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "cinems")
public class Cinema {

    @Id
    private long id;
    private String name;
    private String city;


}
