package com.finals.kinoarena.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finals.kinoarena.model.DTO.IMDBMovieDTO;
import com.finals.kinoarena.util.Constants;
import com.finals.kinoarena.util.exceptions.BadGetawayException;
import com.finals.kinoarena.util.exceptions.BadRequestException;
import com.finals.kinoarena.util.exceptions.NotFoundException;
import com.finals.kinoarena.util.exceptions.UnauthorizedException;
import com.finals.kinoarena.model.DTO.addMovieDTO;
import com.finals.kinoarena.model.DTO.ResponseMovieDTO;
import com.finals.kinoarena.model.entity.Genre;
import com.finals.kinoarena.model.entity.Movie;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
public class MovieService extends com.finals.kinoarena.service.AbstractService {

    public ResponseMovieDTO getMovieById(int movieId) {
        Optional<Movie> sMovie = movieRepository.findById(movieId);
        if (sMovie.isEmpty()) {
            throw new NotFoundException("Movie does not exist");
        }
        return new ResponseMovieDTO(sMovie.get());
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

    public ResponseMovieDTO addMovie(addMovieDTO addMovieDTO, int userId) throws BadRequestException, UnauthorizedException, BadGetawayException, FileNotFoundException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can add movies");
        }
        Movie sMovie = movieRepository.findByTitle(addMovieDTO.getTitle());
        if (sMovie != null) {
            throw new BadRequestException("There is already a movie with that title");
        }
        Optional<Genre> sGenre = genreRepository.findById(addMovieDTO.getGenreId());
        if (sGenre.isEmpty()) {
            throw new BadRequestException("Invalid genre");
        }
        String title = addMovieDTO.getTitle().replaceAll("\\s", "");
        IMDBMovieDTO imdb = new IMDBMovieDTO(getImdbInfo(Constants.API_URL_SEARCH_BY_NAME + title));
        if (imdb.getImdbId().isBlank() || imdb.getImdbId() == null) {
            throw new BadRequestException("Movie with that title does not exist");
        }
        Movie movie = Movie.builder()
                .title(imdb.getTitle())
                .year(imdb.getYear())
                .plot(imdb.getPlot())
                .length(imdb.getLength())
                .rating(imdb.getRating())
                .ageRestriction(addMovieDTO.getAgeRestriction())
                .leadingActor(imdb.getLead())
                .genre(sGenre.get())
                .poster(imdb.getPoster())
                .imdbId(imdb.getImdbId())
                .projections(new ArrayList<>())
                .build();
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

    public JsonNode getImdbInfo(String url) throws BadGetawayException, FileNotFoundException {
        File file = new File("C:\\Users\\user\\Desktop\\kinoarena\\KinoArena\\src\\main\\java\\com\\finals\\kinoarena\\service\\apikey.txt");
        Scanner scanner = new Scanner(file);
        String key = scanner.nextLine();
        scanner.close();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-rapidapi-key",key)
                .header("x-rapidapi-host", "imdb-internet-movie-database-unofficial.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        JsonNode jsonNode;
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper om = new ObjectMapper();
            jsonNode = om.readTree(response.body());
        } catch (Exception e) {
            throw new BadGetawayException("Cannot connect to IMDB,please try again later");
        }
        return jsonNode;
    }
}
