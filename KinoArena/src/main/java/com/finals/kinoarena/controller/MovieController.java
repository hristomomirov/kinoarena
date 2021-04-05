package com.finals.kinoarena.controller;

import com.finals.kinoarena.service.MovieService;
import com.finals.kinoarena.util.exceptions.BadRequestException;
import com.finals.kinoarena.util.exceptions.UnauthorizedException;
import com.finals.kinoarena.model.DTO.*;
import com.finals.kinoarena.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Component
@RestController
public class MovieController extends AbstractController {

    @Autowired
    private MovieService movieService;

    @GetMapping(value = "/movies/{movie_id}")
    public ResponseMovieDTO getMovieById(@PathVariable(name = "movie_id") int movieId) {
        return movieService.getMovieById(movieId);
    }

    @GetMapping(value = "/genre/{genre_id}/movies")
    public List<ResponseMovieDTO> getMovieByGenre(@PathVariable(name = "genre_id") int genreId) {
        return movieService.getMoviesByGenre(genreId);
    }
    @GetMapping(value = "/movies/title/{title}")
    public String findMoviesByName(@PathVariable(name ="title") String title) throws IOException, InterruptedException {
        return movieService.findMovies(title);
    }

    @PostMapping(value = "/movies")
    public ResponseMovieDTO addMovie(@RequestBody RequestMovieDTO requestMovieDTO, HttpSession ses) throws BadRequestException, UnauthorizedException, IOException, InterruptedException {
        User user = sessionManager.getLoggedUser(ses);
        if (!validateMovie(requestMovieDTO)) {
            throw new BadRequestException("Please fill all requested fields");
        }
        return movieService.addMovie(requestMovieDTO, user.getId());
    }

    @DeleteMapping(value = "/movies/{movie_id}")
    public ResponseMovieDTO deleteMovie(@PathVariable(name = "movie_id") int movieId, HttpSession ses) throws UnauthorizedException {
        User user = sessionManager.getLoggedUser(ses);
        int userId = user.getId();
        return movieService.deleteMovie(movieId, userId);
    }


    private boolean validateMovie(RequestMovieDTO dto) throws BadRequestException {
        return validateLength(dto.getLength()) &&
                validateAgeRestriction(dto.getAgeRestriction()) &&
                validateGenre(dto) && validateImdbId(dto.getImdbId());
    }

    private boolean validateImdbId(String imdbId) throws BadRequestException {
        if (!imdbId.isBlank()) {
            return true;
        }
        throw new BadRequestException("Name cannot be empty or more than 200 characters");
    }

    private boolean validateGenre(RequestMovieDTO dto) throws BadRequestException {
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

    private boolean validateLength(int length) throws BadRequestException {
        if (length > 60 && length <= 300) {
            return true;
        }
        throw new BadRequestException("Length must be between 60 and 300 minutes");
    }
}

