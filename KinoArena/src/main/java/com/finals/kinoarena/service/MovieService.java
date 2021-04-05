package com.finals.kinoarena.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finals.kinoarena.model.DTO.IMDBMovieDTO;
import com.finals.kinoarena.util.Constants;
import com.finals.kinoarena.util.exceptions.BadRequestException;
import com.finals.kinoarena.util.exceptions.NotFoundException;
import com.finals.kinoarena.util.exceptions.UnauthorizedException;
import com.finals.kinoarena.model.DTO.RequestMovieDTO;
import com.finals.kinoarena.model.DTO.ResponseMovieDTO;
import com.finals.kinoarena.model.entity.Genre;
import com.finals.kinoarena.model.entity.Movie;;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public ResponseMovieDTO addMovie(RequestMovieDTO requestMovieDTO, int userId) throws BadRequestException, UnauthorizedException, IOException, InterruptedException {
        if (!isAdmin(userId)) {
            throw new UnauthorizedException("Only admins can add movies");
        }
        Movie sMovie = movieRepository.findByTitle(requestMovieDTO.getTitle());
        if (sMovie != null) {
            throw new BadRequestException("There is already a movie with that title");
        }
        Optional<Genre> sGenre = genreRepository.findById(requestMovieDTO.getGenreId());
        if (sGenre.isEmpty()) {
            throw new BadRequestException("Invalid genre");
        }

        String title = requestMovieDTO.getTitle();
        IMDBMovieDTO imdb = getImdbInfo(title);
        if (imdb.getImdbId().isBlank()){
            throw new BadRequestException("Movie with that name does not exist");
        }

        Genre genre = sGenre.get();
        Movie movie = new Movie(requestMovieDTO);

        movie.setGenre(genre);
        movie.setImdbId(imdb.getImdbId());
        movie.setYear(imdb.getYear());
        movie.setLength(imdb.getLength());
        movie.setPlot(imdb.getPlot());
        movie.setRating(imdb.getRating());
        movie.setPoster(imdb.getPoster());
        movie.setLeadingActor(imdb.getLead());

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

    private IMDBMovieDTO getImdbInfo(String title) throws IOException, InterruptedException {

        String url = Constants.API_URL + title.replaceAll("\\s", "");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-rapidapi-key", "fb75dc0fa7mshf37a26ee181e77cp12ffc0jsn8b15cd65c1ee")
                .header("x-rapidapi-host", "imdb-internet-movie-database-unofficial.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper om = new ObjectMapper();
        JsonNode jsonNode = om.readTree(response.body());
        return new IMDBMovieDTO(jsonNode);
    }

    public String findMovies(String title) throws IOException, InterruptedException {
        String url = "https://imdb-internet-movie-database-unofficial.p.rapidapi.com/search/" + title;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-rapidapi-key", "fb75dc0fa7mshf37a26ee181e77cp12ffc0jsn8b15cd65c1ee")
                .header("x-rapidapi-host", "imdb-internet-movie-database-unofficial.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
