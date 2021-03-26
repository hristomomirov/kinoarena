package com.finals.kinoarena.Model.Repository;

import com.finals.kinoarena.Model.Entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    List<Genre> findAllByType(String s);
}

