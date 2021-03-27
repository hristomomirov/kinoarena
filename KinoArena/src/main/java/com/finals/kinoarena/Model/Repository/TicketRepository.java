package com.finals.kinoarena.Model.Repository;

import com.finals.kinoarena.Model.DTO.TicketWithoutUserDTO;
import com.finals.kinoarena.Model.Entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    List<TicketWithoutUserDTO> findAllByOwnerId(int id);

    List<Ticket> findAllByProjectionId(int Id);

}
