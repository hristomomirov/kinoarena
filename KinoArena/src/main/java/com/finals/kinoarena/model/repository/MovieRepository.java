package com.finals.kinoarena.model.repository;

import com.finals.kinoarena.model.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    List<Movie> findAllByGenreId(int genreId);

    Movie findByTitle(String title);

}
