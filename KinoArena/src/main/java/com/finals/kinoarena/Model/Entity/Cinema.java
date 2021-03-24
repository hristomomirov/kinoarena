package com.finals.kinoarena.Model.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

<<<<<<< HEAD
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
=======
import javax.persistence.*;
import java.util.List;
>>>>>>> 1fc85a876f1cd873a74d9f7f3ad995ef007976f3

@NoArgsConstructor
@Getter
@Setter
<<<<<<< HEAD
@Entity
@Table(name = "cinemas")
=======
@Entity(name = "cinemаs")
//@Table(name = "cinemаs"
>>>>>>> 1fc85a876f1cd873a74d9f7f3ad995ef007976f3
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String city;

    public Cinema(String name, String city) {
        this.name = name;
        this.city = city;
    }
}
