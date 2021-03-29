package com.finals.kinoarena.Model.Repository;

import com.finals.kinoarena.Model.Entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    List<Movie> findAllByGenreId(int genreId);

    List<Movie> findAllByTitle(String title);

}
