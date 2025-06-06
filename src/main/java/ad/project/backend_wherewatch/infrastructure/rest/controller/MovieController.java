package ad.project.backend_wherewatch.infrastructure.rest.controller;

import ad.project.backend_wherewatch.application.dto.MovieDTO;
import ad.project.backend_wherewatch.application.mapper.MovieMapper;
import ad.project.backend_wherewatch.application.service.implementation.TmdbService;
import ad.project.backend_wherewatch.domain.models.Movie;
import ad.project.backend_wherewatch.application.service.implementation.MovieService;
import ad.project.backend_wherewatch.domain.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/movies")
public class MovieController {

    private final TmdbService tmdbService;
    private final MovieService movieService;

    @Autowired
    public MovieController(TmdbService tmdbService, MovieService movieService) {
        this.tmdbService = tmdbService;
        this.movieService = movieService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieDTO>> searchMovies(@RequestParam String title) {
        List<MovieDTO> results = tmdbService.searchAndFallback(title);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable int id) {
        Movie movie = movieService.searchMovieById(id);
        return ResponseEntity.ok(movie);
    }

}

