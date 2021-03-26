package com.finals.kinoarena.Model.Repository;

import com.finals.kinoarena.Model.Entity.Hall;
import com.finals.kinoarena.Model.Entity.Projection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProjectionRepository extends JpaRepository<Projection, Integer> {

    List<Projection> findByGenre_Id(int id);
    List<Projection> findByHall_id(int id);
    List<Projection> findByHall(Hall hall);

}
