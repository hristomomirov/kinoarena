package com.finals.kinoarena.model.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatisticsDTO {
    private String cinemaName;
    private int hallNumber;
    private String title;
    private int totalTicketsSold;
}
