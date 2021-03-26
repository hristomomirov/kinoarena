package com.finals.kinoarena.Model.Repository;

import com.finals.kinoarena.Model.Entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre,Integer> {
}
