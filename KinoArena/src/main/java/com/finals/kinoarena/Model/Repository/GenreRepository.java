package com.finals.kinoarena.Model.Repository;

import com.finals.kinoarena.Model.Entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    List<Genre> findAllByType(String s);
=======
public interface GenreRepository extends JpaRepository<Genre,Integer> {
>>>>>>> origin/master
}
