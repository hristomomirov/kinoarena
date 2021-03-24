package com.finals.kinoarena.Model.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tickets")
public class Ticket {

    @Id
    private int id;
    private int ownerId;
    private int cinemaId;
    private int hallId;
    private int projectionId;
    private int row;
    private int seat;
    private LocalDateTime purchasedAt;
}
