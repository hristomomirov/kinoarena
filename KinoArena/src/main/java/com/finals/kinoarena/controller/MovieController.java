package com.finals.kinoarena.controller;

import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Exceptions.UnauthorizedException;
import com.finals.kinoarena.Model.DTO.*;
import com.finals.kinoarena.Model.Entity.User;
import com.finals.kinoarena.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Component
@RestController
public class MovieController {

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private MovieService movieService;

    @GetMapping(value = "/movies/{movie_id}")
    public String  getMovieById(@PathVariable(name = "movie_id") int movieId) throws IOException, InterruptedException {
        return movieService.getMovieById(movieId);
    }

    @GetMapping(value = "/genres/{genre_id}/movies")
    public List<MovieDTO> getMovieByGenre(@PathVariable(name = "genre_id") int genreId) {
        return movieService.getMoviesByGenre(genreId);
    }

    @PutMapping(value = "/movies")
    public MovieDTO addMovie(@RequestBody AddMovieDTO addMovieDTO, HttpSession ses) throws BadRequestException, UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        if (!validateMovie(addMovieDTO)) {
            throw new BadRequestException("Please fill all requested fields");
        }
        return movieService.addMovie(addMovieDTO, user.getId());
    }

    @DeleteMapping(value = "/movies{movie_id}")
    public MovieDTO deleteMovie(@PathVariable(name = "movie_id") int movieId, HttpSession ses) throws UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        int userId = user.getId();
        return movieService.deleteMovie(movieId, userId);
    }

    @PostMapping(value = "/movies{movie_id}")
    public MovieDTO editMovie(@PathVariable(name = "movie_id") int movieId, HttpSession ses, @RequestBody AddMovieDTO addMovieDTO) throws UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        int userId = user.getId();
        return movieService.editMovie(movieId, addMovieDTO, userId);
    }

    private boolean validateMovie(AddMovieDTO dto) throws BadRequestException {
        return validateTitle(dto.getTitle()) &&
                validateLength(dto.getLength()) &&
                validateDescription(dto.getDescription()) &&
                validateAgeRestriction(dto.getAgeRestriction()) &&
                validateGenre(dto);
    }

    private boolean validateGenre(AddMovieDTO dto) throws BadRequestException {
        if (dto.getGenreId() == null) {
            throw new BadRequestException("Please fill all requested fields");
        }
        return true;
    }

    private boolean validateAgeRestriction(int ageRestriction) throws BadRequestException {
        if (ageRestriction >= 3) {
            return true;
        }
        throw new BadRequestException("Age restriction cannot be less than 3");
    }

    private boolean validateDescription(String description) throws BadRequestException {
        if (!description.isBlank()) {
            return true;
        }
        throw new BadRequestException("Description cannot be empty");
    }

    private boolean validateLength(int length) throws BadRequestException {
        if (length > 60 && length <= 300) {
            return true;
        }
        throw new BadRequestException("Length must be between 60 and 300 minutes");
    }

    private boolean validateTitle(String title) throws BadRequestException {
        if (!title.isBlank() && title.length() <= 200) {
            return true;
        }
        throw new BadRequestException("Name cannot be empty or more than 200 characters");
    }
}

