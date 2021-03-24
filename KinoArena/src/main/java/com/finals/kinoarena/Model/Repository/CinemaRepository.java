package com.finals.kinoarena.Model.Repository;

import com.finals.kinoarena.Model.Entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema,Integer> {

    public List<Cinema> findByCity(String city);
    public Cinema findByCityAndName(String city,String name);

}
