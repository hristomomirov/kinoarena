package com.finals.kinoarena.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
public class ReserveTicketDTO {

   @Min(value = 1,message = "Invalid seat number")
   @Max(value = 200,message = "Invalid seat number")
   private Integer seat;
}
