package com.finals.kinoarena.Model.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity

@Table(name = "genres")
public class Genre {

    @Id
    private int id;
    private String type;
    @OneToMany(mappedBy = "genre")
    @JsonBackReference
    private List<Projection>projections;
}
