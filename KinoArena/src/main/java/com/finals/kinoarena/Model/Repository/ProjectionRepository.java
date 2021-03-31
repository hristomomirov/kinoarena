package com.finals.kinoarena.Model.Repository;

import com.finals.kinoarena.Model.Entity.Hall;
import com.finals.kinoarena.Model.Entity.Projection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProjectionRepository extends JpaRepository<Projection, Integer> {


    List<Projection> findByHall(Hall hall);
    List<Projection> findByHallId(int hallId);
    List<Projection> findByMovie_id(int movieID);

}
