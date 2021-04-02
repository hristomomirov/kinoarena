package com.finals.kinoarena.service;

import com.fasterxml.jackson.annotation.JsonValue;
import com.finals.kinoarena.util.exceptions.BadRequestException;
import com.finals.kinoarena.util.exceptions.NotFoundException;
import com.finals.kinoarena.util.exceptions.UnauthorizedException;
import com.finals.kinoarena.model.DTO.RequestMovieDTO;
import com.finals.kinoarena.model.DTO.ResponseMovieDTO;
import com.finals.kinoarena.model.entity.Genre;
import com.finals.kinoarena.model.entity.Movie;
import com.finals.kinoarena.model.repository.GenreRepository;
import com.finals.kinoarena.model.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MovieService extends AbstractService {

    @Autowired
    GenreRepository genreRepository;
    @Autowired
    MovieRepository movieRepository;

    @JsonValue
    public String getMovieById(int movieId) throws IOException, InterruptedException {
        Optional<Movie> sMovie = movieRepository.findById(movieId);
        if (sMovie.isEmpty()) {
            throw new NotFoundException("Movie does not exist");
        }
        String imdbID = movieRepository.findById(movieId).get().getImdbId();
        String url = "https://imdb-internet-movie-database-unofficial.p.rapidapi.com/film/" + imdbID;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-rapidapi-key", "fb75dc0fa7mshf37a26ee181e77cp12ffc0jsn8b15cd65c1ee")
                .header("x-rapidapi-host", "imdb-internet-movie-database-unofficial.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public List<ResponseMovieDTO> getMoviesByGenre(int genreId) {
        List<Movie> sMovies = movieRepository.findAllByGenreId(genreId);
        if (sMovies.isEmpty()) {
            throw new NotFoundException("There are no movies with that genre");
        }
        List<ResponseMovieDTO> movies = new ArrayList<>();
        for (Movie m : sMovies) {
            movies.add(new ResponseMovieDTO(m));
        }
        return movies;
    }


    public ResponseMovieDTO addMovie(RequestMovieDTO requestMovieDTO, int userId) throws BadRequestException, UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can remove cinemas");
        }
        Movie sMovie = movieRepository.findByTitle(requestMovieDTO.getTitle());
        if (sMovie != null) {
            throw new BadRequestException("There is already a movie with that title");
        }
        Optional<Genre> sGenre = genreRepository.findById(requestMovieDTO.getGenreId());
        if (sGenre.isEmpty()) {
            throw new BadRequestException("Invalid genre");
        }
        Genre genre = sGenre.get();
        Movie movie = new Movie(requestMovieDTO);
        movie.setGenre(genre);
        return new ResponseMovieDTO(movieRepository.save(movie));
    }

    public ResponseMovieDTO deleteMovie(int movieId, int userId) throws UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can remove movies");
        }
        Optional<Movie> sMovie = movieRepository.findById(movieId);
        if (sMovie.isEmpty()) {
            throw new NotFoundException("Movie does not exist");
        }
        movieRepository.deleteById(movieId);
        return new ResponseMovieDTO(sMovie.get());
    }

    public ResponseMovieDTO editMovie(int movieId, RequestMovieDTO requestMovieDTO, int userId) throws UnauthorizedException, BadRequestException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can edit halls");
        }
        Optional<Movie> sMovie = movieRepository.findById(movieId);
        if (sMovie.isEmpty()) {
            throw new NotFoundException("Movie is not found");
        }
        Movie movieQuery = movieRepository.findByTitle(requestMovieDTO.getTitle());
        if (movieQuery != null) {
            throw new BadRequestException("There is already a movie with that title");
        }
        Optional<Genre> sGenre = genreRepository.findById(requestMovieDTO.getGenreId());
        if (sGenre.isEmpty()) {
            throw new BadRequestException("Invalid genre");
        }
        Movie movie = sMovie.get();
        Genre genre = sGenre.get();
        movie.setTitle(requestMovieDTO.getTitle());
        movie.setDescription(requestMovieDTO.getDescription());
        movie.setLength(requestMovieDTO.getLength());
        movie.setAgeRestriction(requestMovieDTO.getAgeRestriction());
        movie.setGenre(genre);
        return new ResponseMovieDTO(movieRepository.save(movie));

    }
}
