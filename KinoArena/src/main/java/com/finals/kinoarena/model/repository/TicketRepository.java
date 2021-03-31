package com.finals.kinoarena.model.repository;

import com.finals.kinoarena.model.DTO.ResponseTicketDTO;
import com.finals.kinoarena.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    List<ResponseTicketDTO> findAllByOwnerId(int id);


}
