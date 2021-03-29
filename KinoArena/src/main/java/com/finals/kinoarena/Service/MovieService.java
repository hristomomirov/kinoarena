package com.finals.kinoarena.Service;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Exceptions.NotFoundException;
import com.finals.kinoarena.Exceptions.UnauthorizedException;
import com.finals.kinoarena.Model.DTO.AddMovieDTO;
import com.finals.kinoarena.Model.DTO.HallDTO;
import com.finals.kinoarena.Model.DTO.MovieDTO;
import com.finals.kinoarena.Model.Entity.Cinema;
import com.finals.kinoarena.Model.Entity.Genre;
import com.finals.kinoarena.Model.Entity.Hall;
import com.finals.kinoarena.Model.Entity.Movie;
import com.finals.kinoarena.Model.Repository.GenreRepository;
import com.finals.kinoarena.Model.Repository.MovieRepository;
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

    public List<MovieDTO> getMoviesByGenre(int genreId) {
        List<Movie> sMovies = movieRepository.findAllByGenreId(genreId);
        if (sMovies.isEmpty()) {
            throw new NotFoundException("There are no movies with that genre");
        }
        List<MovieDTO> movies = new ArrayList<>();
        for (Movie m : sMovies) {
            movies.add(new MovieDTO(m));
        }
        return movies;
    }


    public MovieDTO addMovie(AddMovieDTO addMovieDTO, int userId) throws BadRequestException, UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can remove cinemas");
        }
        List<Movie> movies = movieRepository.findAllByTitle(addMovieDTO.getTitle());
        if (!movies.isEmpty()) {
            throw new BadRequestException("There is already a movie with that title");
        }
        Optional<Genre> sGenre = genreRepository.findById(addMovieDTO.getGenreId());
        if (sGenre.isEmpty()) {
            throw new BadRequestException("Invalid genre");
        }
        Genre genre = sGenre.get();
        addMovieDTO.setGenre(genre);
        Movie movie = new Movie(addMovieDTO);
        return new MovieDTO(movieRepository.save(movie));

    }

    public MovieDTO deleteMovie(int movieId, int userId) throws UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can remove movies");
        }
        Optional<Movie> sMovie = movieRepository.findById(movieId);
        if (sMovie.isEmpty()) {
            throw new NotFoundException("Movie does not exist");
        }
        movieRepository.deleteById(movieId);
        return new MovieDTO(sMovie.get());
    }

    public MovieDTO editMovie(int movieId, AddMovieDTO addMovieDTO, int userId) throws UnauthorizedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can edit halls");
        }
        Optional<Movie> sMovie = movieRepository.findById(movieId);
        if (sMovie.isEmpty()) {
            throw new NotFoundException("Movie is not found");
        }
        Movie movie = sMovie.get();
        movie.setTitle(addMovieDTO.getTitle());
        movie.setDescription(addMovieDTO.getDescription());
        movie.setLength(addMovieDTO.getLength());
        movie.setAgeRestriction(addMovieDTO.getAgeRestriction());
        movie.setGenre(addMovieDTO.getGenre());
        return new MovieDTO(movieRepository.save(movie));

    }
}
